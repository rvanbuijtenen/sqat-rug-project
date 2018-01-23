module sqat::series2::A1b_DynCov

import Java17ish;
import ParseTree;
import util::FileSystem;
import Message;
import lang::java::jdt::m3::AST;
import IO;
import lang::csv::IO;
import ValueIO;
import lang::java::m3::Core;
import Type;
import List;
import Message;
import String;

/*

Assignment: instrument (non-test) code to collect dynamic coverage data.

- Write a little Java class that contains an API for collecting coverage information
  and writing it to a file. NB: if you write out CSV, it will be easy to read into Rascal
  for further processing and analysis (see here: lang::csv::IO)

- Write two transformations:
  1. to obtain method coverage statistics
     (at the beginning of each method M in class C, insert statement `hit("C", "M")`
  2. to obtain line-coverage statistics
     (insert hit("C", "M", "<line>"); after every statement.)

The idea is that running the test-suite on the transformed program will produce dynamic
coverage information through the insert calls to your little API.

Questions
- use a third-party coverage tool (e.g. Clover) to compare your results to (explain differences)
	Eclipse coverage says 80%.
- which methods have full line coverage?
- which methods are not covered at all, and why does it matter (if so)?
- what are the drawbacks of source-based instrumentation?
	1) Computationally expensive (parse entire source, run all test cases, parse produced output)
	2) Requires a working version of the source code. This is not always an easy task if the code was supplied by a third party

Tips:
- create a shadow JPacman project (e.g. jpacman-instrumented) to write out the transformed source files.
  Then run the tests there. You can update source locations l = |project://jpacman/....| to point to the 
  same location in a different project by updating its authority: l.authority = "jpacman-instrumented"; 

- to insert statements in a list, you have to match the list itself in its context, e.g. in visit:
     case (Block)`{<BlockStm* stms>}` => (Block)`{<BlockStm insertedStm> <BlockStm* stms>}` 
  
- or (easier) use the helper function provide below to insert stuff after every
  statement in a statement list.

- to parse ordinary values (int/str etc.) into Java15 syntax trees, use the notation
   [NT]"...", where NT represents the desired non-terminal (e.g. Expr, IntLiteral etc.).  

*/
alias C = map[str,map[str,list[str]]];

rel[str class,str method,str line] r = {};

int insertedCnt = 0;
void main(str coverageLevel) {
	loc project = |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/|;
	rel[loc, Tree, int, int] augmented_files = {augmentFile(f, coverageLevel) | f <- files(project)};
	saveProject(augmented_files);
	writeCSV(r, |project://jpacman-instrumented/inserted.csv|);
}

void saveProject(rel[loc, Tree, int, int] augmentedFiles) {
	for(f <- augmentedFiles) {
		saveFile(f);
	}
	Tree api = parse(#start[CompilationUnit], |project://sqat-analysis/src/sqat/series2/Api.java|, allowAmbiguity=true);
	loc apiCopyLoc = |project://jpacman-instrumented/src/main/java/nl/tudelft/jpacman/Api.java|;
	writeFile(apiCopyLoc, api);
}

void saveFile(<loc l, Tree t, int insertedMethodCnt, int insertedStmtCnt>) {
	println("saving file" + l.path);
	writeFile(l, t);
}

tuple[loc, Tree, int, int] augmentFile(loc file, str coverageLevel) {
	println("parsing file" + file.path);
	Tree t = parse(#start[CompilationUnit], file, allowAmbiguity=true);
	t = addApiImport(t);
	switch(coverageLevel) {
		case "method": {
			t = augmentMethods(t);
		}
		case "statement": {
			t = augmentStatements(t);
		}
	}
	<methodCnt, statementCnt> = count(t);
	file.authority = "jpacman-instrumented";
	return <file, t, methodCnt, statementCnt>;
}

Tree addApiImport(Tree t) {
	api = (ImportDec)`import nl.tudelft.jpacman.Api;`;
	return top-down-break visit(t) {
		case (CompilationUnit) `<PackageDec? package> <ImportDec* imports> <TypeDec* types>` => (CompilationUnit)`<PackageDec? package> <ImportDec* imports> <ImportDec api> <TypeDec* types>`
	}
}

Tree augmentStatements(Tree t) {
	Id name = (Id)`none`;
	
	return bottom-up visit(t) {
		case (MethodDecHead) `<MethodDecHead h>`: {
			name = getMethodName(h);
		}
		case (MethodBody) `{<BlockStm* stms>}` => blockify(putBeforeEvery(stms, callStmt, name)) when countStatements(stms) > 0
	}
}

Tree augmentMethods(Tree t) {
	Id name = (Id)`none`;
	return bottom-up visit(t) {
		case (MethodDecHead) `<MethodDecHead h>`: {
			name = getMethodName(h);
		} 
		case (MethodBody) `<MethodBody b>` => putInMethod(b, callMthd, name)
	}
}

tuple[int, int] count(Tree tree) {
	tuple[int mCnt, int lCnt] cnts = <0, 0>;
	visit(tree) {
		case (MethodBody) `{<BlockStm* stms>}`: {
			cnts[0] += 1;
			cnts[1] += countStatements(stms);
		}
	}
	return cnts;
}

int countStatements((BlockStm)`<BlockStm* stms>`) {
	cnt = 0;
	bottom-up visit(stms) {
		case (BlockStm)`<BlockStm s>`:{
			cnt += 1;
		}
	}
	return cnt;
}

int countStatements((MethodBody)`;`) = 0;

Id getMethodName(MethodDecHead m) {
	Id currentAnno = (Id)`a`;
	top-down visit(m) {
		case (Anno) `@<Id name>`: {
			currentAnno = name;
		}
		case (Id) `<Id name>`: {
			if(currentAnno != name) {
				if(unparse(name)[0] notin [
				"A","B","C","D","E","F","G","H","I","J","K","L","M",
				"N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]) {
					return name;
				}
			}
		}
	}
}

StringLiteral getClass(loc l) {
	StringPart sp = parse(#StringPart, l.path);
	return (StringLiteral)`"<StringPart sp>"`;
}

StringLiteral getSrcLoc(loc l) {
	StringPart sp = parse(#StringPart, "lines <l.begin.line>:<l.begin.column> - <l.end.line>:<l.end.column>");
	return (StringLiteral)`"<StringPart sp>"`;
}

BlockStm callStmt(loc l, Id name) {
	StringPart sp = parse(#StringPart, unparse(name));
	StringLiteral c = getClass(l);
	StringLiteral m = (StringLiteral)`"<StringPart sp>"`;
	StringLiteral src = getSrcLoc(l);
	return (BlockStm)`Api.hit(<StringLiteral c>, <StringLiteral m>, <StringLiteral src>);`;
}

BlockStm callMthd(loc l, Id name) {
	StringPart sp = parse(#StringPart, unparse(name));
	StringLiteral c = getClass(l);
	StringLiteral m = (StringLiteral)`"<StringPart sp>"`;
	return (BlockStm)`Api.hit(<StringLiteral c>, <StringLiteral m>);`;
}

MethodBody blockify(BlockStm* stms) = (MethodBody)`{<BlockStm* stms>}`;

bool logMethod(loc l, str name) {
	r += <l.path, name, "">;
	return true;
}

bool logStatement(loc l, str name) {
	line = "lines <l.begin.line>:<l.begin.column> - <l.end.line>:<l.end.column>";
	r += <l.path, name, line>;
	return true;
}

MethodBody putInMethod(MethodBody b, BlockStm(loc, Id) f, Id name) {
	MethodBody put(b:(MethodBody)`{}`) = (MethodBody)`{<BlockStm s>}`
		when BlockStm s := f(b@\loc, name),
			logMethod(b@\loc, unparse(name));
		//logMethod(b@\loc);
	MethodBody put(b:(MethodBody)`;`) = (MethodBody)`;`;
	MethodBody put(b:(MethodBody)`{<BlockStm s0> <BlockStm* stms>}`) = (MethodBody)`{<BlockStm s> <BlockStm s0> <BlockStm* stms>}`
    	when BlockStm s := f(s0@\loc, name),
    		logMethod(s0@\loc, unparse(name));
    
   	return put(b);
}

// Helper function to deal with concrete statement lists
// second arg should be a closure taking a location (of the element)
// and producing the BlockStm to-be-inserted 
BlockStm* putBeforeEvery(BlockStm* stms, BlockStm(loc, Id) f, Id name) {
  Block put(b:(Block)`{}`) = (Block)`{<BlockStm s>}`
    when BlockStm s := f(b@\loc, name),
    	logStatement(b@\loc, unparse(name));
  
  Block put((Block)`{<BlockStm s0>}`) = (Block)`{<BlockStm s> <BlockStm s0>}`
    when BlockStm s := f(s0@\loc, name),
    	logStatement(s0@\loc, unparse(name));
  
  Block put((Block)`{<BlockStm s0> <BlockStm+ stms>}`) 
    = (Block)`{<BlockStm s> <BlockStm s0> <BlockStm* stms2>}`
    when
      BlockStm s := f(s0@\loc, name), 
      (Block)`{<BlockStm* stms2>}` := put((Block)`{<BlockStm+ stms>}`),
    	logStatement(s0@\loc, unparse(name));

  if ((Block)`{<BlockStm* stms2>}` := put((Block)`{<BlockStm* stms>}`)) {
    return stms2;
  }
}

C parseCsvToMap(rel[str,str,str] r) {
	C c = ();
	for(<i,j,k> <- r) {
		if(i in c && j in c[i] && k notin c[i][j]) {
			c[i][j] += k;
		}
		if(i in c && j notin c[i]) {
			c[i] += (j:[k]);
		}
		if(i notin c) {
			c[i] = (j:[k]);
		}
	}
	return c;
}

alias coverage = rel[tuple[str,str], int, int, real];

tuple[coverage, real] getCoverage(C c1, C c2) {
	coverage cov = {};
	int totalInserted = 0;
	int totalCovered = 0;
	for(class <- c1) {
		for(method <- c1[class]) {
			if(class in c2 && method in c2[class]) {
				int insertedCnt = size(c1[class][method]);
				int coveredCnt = size(c2[class][method]);
				totalInserted += insertedCnt;
				totalCovered += coveredCnt;
				real covPercent = (coveredCnt * 100.0) / insertedCnt;
				cov += <<class, method>, insertedCnt, coveredCnt, covPercent>;
			}
			if(class in c2 && method notin c2[class] || class notin c2) {
				int insertedCnt = size(c1[class][method]);
				totalInserted += insertedCnt;
				int coveredCnt = 0;
				real covPercent = (coveredCnt * 100.0) / insertedCnt;
				cov += <<class, method>, insertedCnt, coveredCnt, covPercent>;
			}
		}
	}
	return <cov, (totalCovered * 100.0) / totalInserted>;
}

void answerQuestions() {
	loc insertedLoc = |project://jpacman-instrumented/inserted.csv|;
	loc apiOutput = |project://jpacman-instrumented/apiOutput.csv|;
	rel[str,str,str] inserted = readCSV(#rel[str,str,str], insertedLoc);
	C cin = parseCsvToMap(inserted);
	rel[str,str,str] output = readCSV(#rel[str,str,str], apiOutput);
	C cout = parseCsvToMap(output);
	<cov, totalCov> = getCoverage(cin, cout);
	for(<<a,b>,d,e,f> <- cov) {
		if (f < 100.0) {
			println("<a> <b> <d>:<e> <f>");
		}
	}
	
	println("use a third-party coverage tool (e.g. Clover) to compare your results to (explain differences)");
	println("Eclipses \'Coverage as\' option runs all test cases and reports an instruction coverage of 81.7%. Since our program computes a dynamic statement coverage of 81.86% there is no significant difference.");
	println("\n\nwhich methods have full line coverage?");
	for(<<a,b>,d,e,f> <- cov) {
		if (f == 100.0) {
			println("<a> <b> <d>:<e> <f>");
		}
	}
	
	println("\n\nwhich methods are not covered at all, and why does it matter (if so)?");
	for(<<a,b>,d,e,f> <- cov) {
		if (f == 0.0) {
			println("<a> <b> <d>:<e> <f>");
		}
	}
	println("\n\nwhat are the drawbacks of source-based instrumentation?");
	println("1) Computationally expensive (parse entire source, run all test cases, parse produced output)");
	println("2) Requires a working version of the source code. This is not always an easy task if the code was supplied by a third party");
	println("\n\nThe total covereage is: <totalCov>");
}

