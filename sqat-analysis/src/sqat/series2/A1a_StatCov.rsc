module sqat::series2::A1a_StatCov

import lang::java::jdt::m3::Core;
import Java17ish;
import Message;
import lang::java::jdt::m3::AST;
import IO;
import ParseTree;
import Type;
import List;
import Message;
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

*/


M3 jpacmanM3() = createM3FromEclipseProject(|project://jpacman-framework|);


alias JpacmanInvocations = set[tuple[loc, loc]];
alias Graph = rel[loc, str, loc];
alias Edge = rel[Node,Label,Node];
alias Node = loc;
alias Label = str;

void main() {
	M3 x = jpacmanM3();
	invocations = x.methodInvocation;
	jpacman_invocations = getJpacmanInvocations(invocations);
	Graph result = jpacman_invocations;
	result = solve(jpacman_invocations) {
		result = {<f1, l1, t2> | <f1,l1,t1> <- result, <f2, l2, t2> <- jpacman_invocations, f1 != t2, t1 == f2};
	}
	coverage = getTestCoverage(result, getMethods(x.declarations));
	println("Test covereage is:");
	println(coverage);
	answerQuestions();
}

void answerQuestions(){

	print( "what methods are not covered at all?\n" );
	print( "how do your results compare to the jpacman results in the paper? Has jpacman improved?\n" );
	print( "use a third-party coverage tool (e.g. Clover) to compare your results\n" );
	print( "We used the tool Emma, which determines the coverage level based on instructions. They report a test coverage of 40.6%\n" );

}

rel[loc,loc] getMethods(rel[loc,loc] decls) {
	return {<name, src> | <name, src> <- decls, /java\+method/ := name.uri};
}

real getTestCoverage(Graph g, rel[loc, loc] methods) {
	map[loc, bool] m = ();
	for(<name, src> <- methods) {
		m[name] = false;
	}
	for(<from,label,to> <- g) {
		if(isTest(from)) {
			m[to] = true;
		}
	}
	total = 0.0;
	tested = 0.0;
	for(src <- m) {
		total+= 1;
		tested += m[src] == true ? 1 : 0;
	}
	return  tested/total*100;
}

Graph getJpacmanInvocations(invocations) {
	Graph jpacman_invocations = {};
	for(<from, to> <- invocations) {
		if(!isExternalSource(to)){
			if(isTest(from)) {
				jpacman_invocations += <from, "test", to>;
			} else {
				jpacman_invocations += <from, "call", to>;
			}
		}	
	}
	return jpacman_invocations;
}

bool isExternalSource(loc l) {
	return !(/nl\/tudelft\/jpacman/ := l.uri);
}

bool isTest(loc l) {
	return (/test/ := l.uri) || (/Test/ := l.uri);
}

set[tuple[loc, str]] getJpacmanMethods() {
	set[tuple[loc src, str name]] methods = {};
	set[Declaration] decls = createAstsFromEclipseProject(|project://jpacman-framework/|, true);
	visit(decls) {
		case m:\method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	methods += <m.src, name>;
	    }
	    case m:\method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions): {
	    	methods += <m.src, name>;
	    }
	    case c:\constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl):{
	    	methods += <c.src, name>;
	    }
	}
	return {<src, name> | <src, name> <- methods, !isTest(src), !isExternalSource(src)};
}

/**
 //* Step 1: remove calls to external sources
 //* Step 2: locate test cases
 * Step 3: create a graph set[Edge] where Edge = [Node, Label, Node]
 * Step 4: compute transitive closure (set[Edge]) on the graph until no new reachable nodes are added.
 * Step 5: extract all nodes that are present in the transitive closure
 * Step 6: compute tested_nodes = set[Node] all_nodes - set[Node] reachable_nodes
 */
