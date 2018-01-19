module sqat::series2::A1b_DynCov

import Java17ish;
import ParseTree;
import util::FileSystem;
import Message;
import lang::java::jdt::m3::AST;
import IO;
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
- which methods have full line coverage?
- which methods are not covered at all, and why does it matter (if so)?
- what are the drawbacks of source-based instrumentation?

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
void main() {
	loc project = |project://jpacman-instrumented/src/main/java/nl/tudelft/jpacman/Launcher.java|;
	tree = parse(#start[CompilationUnit], project, allowAmbiguity=true);
	//println(tree);
	
	call = (BlockStm)`Api.call("test123");`;
	api = (ImportDec)`import nl.tudelft.jpacman.Api;`;
	x = visit(tree) {
		case (MethodBody) `{<BlockStm* stms>}` => (MethodBody)`{<BlockStm call> <BlockStm* stms>}`
	}
	x = top-down-break visit(tree) {
		case (CompilationUnit) `<PackageDec? package> <ImportDec* imports> <TypeDec* types>` => (CompilationUnit)`<PackageDec? package> <ImportDec* imports> <ImportDec api> <TypeDec* types>`
	}
	cnt1 = 0;
	x2 = visit(tree) {
		case (MethodBody) `{<BlockStm* stms>}` => blockify(putAfterEvery(stms, callStmt, name))
	}
	cnt2 = 0;
	visit(tree) {
		case (Stm) `<Stm s>`: {
			cnt2 += 1;
		}
	}
	//println(x2);
	//println(x2);
	println(cnt2);
	//methodCoverage(project);
	//lineCoverage(project);
}

Id getMethodName(MethodDecHead m) {
	Id currentAnno = (Id)`a`;
	top-down visit(m) {
		case (Anno) `@<Id name>`: {
			currentAnno = name;
		}
		case (Id) `<Id name>`: {
			if(currentAnno != name) {
				return name;
			}
		}
	}
}

StringLiteral getClass(loc l) {
	StringPart sp = parse(#StringPart, l.path);
	return (StringLiteral)`"<StringPart sp>"`;
}

StringLiteral getMethod(loc l) = (StringLiteral)`"unknown"`;

StringLiteral getSrcLoc(loc l) {
	StringPart sp = parse(#StringPart, "lines <l.begin.line>:<l.begin.column> - <l.end.line>:<l.end.column>");
	return (StringLiteral)`"<StringPart sp>"`;
}


BlockStm callStmt(loc l) {
	StringLiteral c = getClass(l);
	StringLiteral m = getMethod(l);
	StringLiteral src = getSrcLoc(l);
	return (BlockStm)`Api.hit(<StringLiteral c>, <StringLiteral m>, <StringLiteral src>);`;
}
BlockStm callMthd(loc l) = (BlockStm)`Api.hit("Method");`;
MethodBody blockify(BlockStm* stms) {
	return (MethodBody)`{<BlockStm* stms>}`;
}

void methodCoverage(loc project) {
	set[Declaration] decls = createAstsFromEclipseProject(project, true);
	BlockStm stms = [];
	insertedStm = (BlockStm)`call();`;
	x = visit(decls){
		case (Block) `{<BlockStm* stms>}` => putAfterEvery(stms, callMethod)
	}
  // to be done
}

void lineCoverage(loc project) {
  // to be done
}



// Helper function to deal with concrete statement lists
// second arg should be a closure taking a location (of the element)
// and producing the BlockStm to-be-inserted 
BlockStm* putAfterEvery(BlockStm* stms, BlockStm(loc) f) {
  
  Block put(b:(Block)`{}`) = (Block)`{<BlockStm s>}`
    when BlockStm s := f(b@\loc);
  
  Block put((Block)`{<BlockStm s0>}`) = (Block)`{<BlockStm s0> <BlockStm s>}`
    when BlockStm s := f(s0@\loc);
  
  Block put((Block)`{<BlockStm s0> <BlockStm+ stms>}`) 
    = (Block)`{<BlockStm s0> <BlockStm s> <BlockStm* stms2>}`
    when
      BlockStm s := f(s0@\loc), 
      (Block)`{<BlockStm* stms2>}` := put((Block)`{<BlockStm+ stms>}`);

  if ((Block)`{<BlockStm* stms2>}` := put((Block)`{<BlockStm* stms>}`)) {
    return stms2;
  }
}


