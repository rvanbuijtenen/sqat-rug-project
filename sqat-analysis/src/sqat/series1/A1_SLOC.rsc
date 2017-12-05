module sqat::series1::A1_SLOC

import IO;
import ParseTree;
import String;
import util::FileSystem;
import Java17ish;
import lang::java::m3::AST;
import lang::java::m3::Core;
/* 

Count Source Lines of Code (SLOC) per file:
- ignore comments
- ignore empty lines

Tips
- use locations with the project scheme: e.g. |project:///jpacman/...|
- functions to crawl directories can be found in util::FileSystem
- use the functions in IO to read source files

Answer the following questions:
- what is the biggest file in JPacman?
- what is the total size of JPacman?
- is JPacman large according to SIG maintainability?
- what is the ratio between actual code and test code size?

Sanity checks:
- write tests to ensure you are correctly skipping multi-line comments
- and to ensure that consecutive newlines are counted as one.
- compare you results to external tools sloc and/or cloc.pl

Bonus:
- write a hierarchical tree map visualization using vis::Figure and 
  vis::Render quickly see where the large files are. 
  (https://en.wikipedia.org/wiki/Treemapping) 
*/

alias SLOC = map[loc file, int sloc];

SLOC sloc(loc project) {
  SLOC result = ();
  for (f <- files(project), f.extension == "java") {
	try {
	  list[str] lines = readFileLines(f);
      start[CompilationUnit] m = parseJava(f);
      println(lines);
      
      
    }
    catch ParseError(loc l): {
      println("Parse error: <l>");
    }
  }
  return result;
}


public int countComment(list[str] file){	
  n = 0;
  for(s <- file)
    if(/\*(.|[\r\n])*?\*/ := s)  
      n +=1;
  return n;
}

     