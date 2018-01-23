module sqat::series2::A1a_StatCov

import lang::java::jdt::m3::Core;
import Java17ish;
import Message;
import lang::java::jdt::m3::AST;
import IO;
import ParseTree;
import Type;
import List;
import String;
import Set;
import Map;
/*
Implement static code coverage metrics by Alves & Visser 
(https://www.sig.eu/en/about-sig/publications/static-estimation-test-coverage)


The relevant base data types provided by M3 can be found here:

- module analysis::m3::Core:

rel[loc name, loc src]        M3.declarations;            // maps declarations to where they are declared. contains any kind of data or type or code declaration (classes, fields, methods, variables, etc. etc.)
rel[loc name, TypeSymbol typ] M3.types;                   // assigns types to declared source code artifacts
rel[loc src, loc name]        M3.uses;                    // maps source locations of usages to the respective declarations
rel[loc from, loc to]         M3.containment;             // what is logically contained in what else (not necessarily physically, but usually also)
list[Message]                 M3.messages;                // error messages and warnings produced while constructing a single m3 model
rel[str simpleName, loc qualifiedName]  M3.names;         // convenience mapping from logical names to end-user readable (GUI) names, and vice versa
rel[loc definition, loc comments]       M3.documentation; // comments and javadoc attached to declared things
rel[loc definition, Modifier modifier]  M3.modifiers;     // modifiers associated with declared things

- module  lang::java::m3::Core:

rel[loc from, loc to] M3.extends;            // classes extending classes and interfaces extending interfaces
rel[loc from, loc to] M3.implements;         // classes implementing interfaces
rel[loc from, loc to] M3.methodInvocation;   // methods calling each other (including constructors)
rel[loc from, loc to] M3.fieldAccess;        // code using data (like fields)
rel[loc from, loc to] M3.typeDependency;     // using a type literal in some code (types of variables, annotations)
rel[loc from, loc to] M3.methodOverrides;    // which method override which other methods
rel[loc declaration, loc annotation] M3.annotations;

Tips
- encode (labeled) graphs as ternary relations: rel[Node,Label,Node]
- define a data type for node types and edge types (labels) 
- use the solve statement to implement your own (custom) transitive closure for reachability.

Questions:
- what methods are not covered at all?
- how do your results compare to the jpacman results in the paper? Has jpacman improved?
- use a third-party coverage tool (e.g. Clover) to compare your results to (explain differences)
We used the tool Emma, which determines the coverage level based on instructions. They report a test coverage of 40.6%
We also used the integrated test coverage from eclipse, by running the jpacman prohect using the "Coverage as" option. This dynamic result lists 80.7% statement coverage.
*/


M3 jpacmanM3() = createM3FromEclipseProject(|project://jpacman-framework/|);


alias JpacmanInvocations = set[tuple[loc, loc]];
alias Graph = rel[loc, str, loc];
alias Edge = rel[Node,Label,Node];
alias Node = loc;
alias Label = str;

alias Graph2 = rel[Node,Node];

void main() {
	M3 m3 = jpacmanM3();
	Graph2 g;
	
	tst = getMethods(m3);
	g = buildGraph(m3, tst);
	Graph2 closure = transitiveClosure(g);
	
	coverage = getCoverage(closure, tst);
	answerQuestions(coverage);
}

tuple[set[loc],set[loc]] getCoverage(Graph2 closure, map[loc, loc] tst) {
	set[loc] coveredmethods = {};
	set[loc] allmethods = {};
	for(<from,to> <- closure) {
		if(isTest(from, tst) && !isTest(to, tst)) {
			if(to notin coveredmethods) {
				coveredmethods += to;
			}
		}
		if(to notin allmethods && !isTest(to, tst)) {
			allmethods += to;
		}
	}
	return <coveredmethods, allmethods>;
}

Graph2 transitiveClosure(Graph2 g) {
	Graph2 result = g;
	solve(g) {
		int s = 0;
		int new_s = 0;
		s = size(result);
		result = result + {<f1,t2> | <f1,t1> <-result, <f2,t2> <- result, t1 == f2, <f1,t2> notin result};
		new_s = size(result);
		while(new_s > s) {
			s = new_s;
			result = result + {<f1,t2> | <f1,t1> <-result, <f2,t2> <- result, t1 == f2, <f1,t2> notin result};
			new_s = size(result);
		}
	}
	return result;
}

bool isJpacman(loc l) = contains(l.path, "/jpacman/");

map[loc, loc] getMethods(M3 m3) {
	map[loc, loc] tst = ();
	for(decl <- m3.declarations) {
		if(isMethod(decl.name)) {
			if(isJpacman(decl.name)) {
				if(contains(decl.src.path, "/test/")) {
					tst += (decl.name: decl.src);
				}
			}
		}
	}
	return tst;
}

bool isTest(loc l, map[loc, loc] tst) = l in tst ? true : false;

Graph2 buildGraph(M3 m3, map[loc, loc] tst) {
	Graph2 g = {};
	for(<from, to> <- m3.methodInvocation) {
		if(isJpacman(to)) {
			g += <from, to>;
		}
	}
	return g;
}

void answerQuestions(tuple[set[loc],set[loc]] coverage){
	println("what methods are not covered at all?");
	for(method <- coverage[1] - coverage[0]) {
		println(method);
	}
	println("how do your results compare to the jpacman results in the paper? Has jpacman improved?");
	println("The paper reports a static covereage of 84.53%");
	println("Our static coverage is: <size(coverage[0]) * 100.0 / size(coverage[1])>");
	println("So if our covereage is correct, then jpacman\'s test coverage now is slightly worse than it was in the paper");
	println("use a third-party coverage tool (e.g. Clover) to compare your results to (explain differences)");
	println("We used the integrated test coverage from eclipse, by running the jpacman project using the \"Coverage as\" option. This dynamic result lists 81.7% statement coverage. This difference is caused because a different approach is used by eclipse: they use dynamic covereage which computes instruction coverage, while we\'re looking at method covereage");
}

