module sqat::series2::A1b_DynCov

import Java17ish;
import ParseTree;
import util::FileSystem;
import Message;
import lang::java::jdt::m3::AST;
import IO;
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
int insertedCnt = 0;
void main(str coverageLevel) {
	loc project = |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/|;
	rel[loc, Tree, int, int] augmented_files = {augmentFile(f, coverageLevel) | f <- files(project)};
	commitProject(augmented_files);
}

void commitProject(rel[loc, Tree, int, int] augmentedFiles) {
	for(f <- augmentedFiles) {
		commitFile(f);
	}
	Tree api = parse(#start[CompilationUnit], |project://sqat-analysis/src/sqat/series2/Api.java|, allowAmbiguity=true);
	loc apiCopyLoc = |project://jpacman-instrumented/src/main/java/nl/tudelft/jpacman/Api.java|;
	writeFile(apiCopyLoc, api);
}

void commitFile(<loc l, Tree t, int insertedMethodCnt, int insertedStmtCnt>) {
	println("saving file");
	println(l);
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
/*
void main(str mode) {
	loc project = |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/|;
	switch(mode) {
		case "augment": {
			augment(project);
		}
		case "coverage": {
			//methodCoverage(project);
			//lineCoverage(project);
			;
		}
	}
}

void augmentProject(loc project) {
	set[rel[loc, Tree]] augmented_files = {augmentFile(f) | f <- files(project)};
	tree = parse(#start[CompilationUnit], project, allowAmbiguity=true);
	<mCnt, lCnt> = count(tree);
	println(mCnt);
	println(lCnt);
}

rel[loc, Tree] augmentFile(loc file) {
	rel[loc, Tree t];
}
*/
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
/*
void main() {
	loc project = |project://jpacman-instrumented/src/main/java/nl/tudelft/jpacman/Launcher.java|;
	tree = parse(#start[CompilationUnit], project, allowAmbiguity=true);
	//println(tree);
	
	call = (BlockStm)`Api.call("test123");`;
	api = (ImportDec)`import nl.tudelft.jpacman.Api;`;
	
	x = top-down-break visit(tree) {
		case (CompilationUnit) `<PackageDec? package> <ImportDec* imports> <TypeDec* types>` => (CompilationUnit)`<PackageDec? package> <ImportDec* imports> <ImportDec api> <TypeDec* types>`
	}
	Id name = (Id)`none`;
	methodCnt = 0;
	statementCnt = 0;
	visit(x) {
		case (MethodBody) `<MethodBody b>`: {
			methodCnt += 1;
			println(countStatements(b));
			statementCnt += countStatements(b);
		}
	}
	x = top-down visit(x) {
		case (MethodDecHead) `<MethodDecHead h>`: {
			name = getMethodName(h);
		}
		case (MethodBody) `{<BlockStm* stms>}` => blockify(putBeforeEvery(stms, callStmt, name))
		
	}
	x = top-down visit(x) {
		case (MethodDecHead) `<MethodDecHead h>`: {
			name = getMethodName(h);
		} 
		case (MethodBody) `<MethodBody b>` => putInMethod(b, callMthd, name)
	}
	
	
	

	println(x);
	//println(x);
	println(methodCnt);
	println(statementCnt);
	//methodCoverage(project);
	//lineCoverage(project);
}
*/
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
				if(unparse(name)[0] notin ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]) {
					println(name);
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
	str s = unparse(name);
	StringPart sp = parse(#StringPart, s);
	StringLiteral c = getClass(l);
	StringLiteral m = (StringLiteral)`"<StringPart sp>"`;
	StringLiteral src = getSrcLoc(l);
	return (BlockStm)`Api.hit(<StringLiteral c>, <StringLiteral m>, <StringLiteral src>);`;
}

BlockStm callMthd(loc l, Id name) {
	str s = unparse(name);
	StringPart sp = parse(#StringPart, s);
	StringLiteral c = getClass(l);
	StringLiteral m = (StringLiteral)`"<StringPart sp>"`;
	return (BlockStm)`Api.hit(<StringLiteral c>, <StringLiteral m>);`;
}

MethodBody blockify(BlockStm* stms) = (MethodBody)`{<BlockStm* stms>}`;

void methodCoverage(loc project) {
	set[Declaration] decls = createAstsFromEclipseProject(project, true);
	BlockStm stms = [];
	insertedStm = (BlockStm)`call();`;
	x = visit(decls){
		case (Block) `{<BlockStm* stms>}` => putBeforeEvery(stms, callMethod)
	}
  // to be done
}

void lineCoverage(loc project) {
  // to be done
}

MethodBody putInMethod(MethodBody b, BlockStm(loc, Id) f, Id name) {
	MethodBody put(b:(MethodBody)`{}`) = (MethodBody)`{<BlockStm s>}`
		when BlockStm s := f(b@\loc, name);
	MethodBody put(b:(MethodBody)`;`) = (MethodBody)`;`;
	MethodBody put(b:(MethodBody)`{<BlockStm s0> <BlockStm* stms>}`) = (MethodBody)`{<BlockStm s> <BlockStm s0> <BlockStm* stms>}`
    	when BlockStm s := f(s0@\loc, name);
    
   	return put(b);
}

// Helper function to deal with concrete statement lists
// second arg should be a closure taking a location (of the element)
// and producing the BlockStm to-be-inserted 
BlockStm* putBeforeEvery(BlockStm* stms, BlockStm(loc, Id) f, Id name) {
  Block put(b:(Block)`{}`) = (Block)`{<BlockStm s>}`
    when BlockStm s := f(b@\loc, name);
  
  Block put((Block)`{<BlockStm s0>}`) = (Block)`{<BlockStm s> <BlockStm s0>}`
    when BlockStm s := f(s0@\loc, name);
  
  Block put((Block)`{<BlockStm s0> <BlockStm+ stms>}`) 
    = (Block)`{<BlockStm s> <BlockStm s0> <BlockStm* stms2>}`
    when
      BlockStm s := f(s0@\loc, name), 
      (Block)`{<BlockStm* stms2>}` := put((Block)`{<BlockStm+ stms>}`);

  if ((Block)`{<BlockStm* stms2>}` := put((Block)`{<BlockStm* stms>}`)) {
    return stms2;
  }
}


/*
TODO:
	- copy api to correct location
	- save modified source to jpacman-instrumented
	- run test cases
	- read produced .csv
	- use .csv data to compute test coverage
	- run test cases from rascal?
*/
