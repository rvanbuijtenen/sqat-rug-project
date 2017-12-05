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
	Level.java is the larges file with 179 LOC.
- what is the total size of JPacman?
	According to our program the total size of JPacman is 1900 LOC.
- is JPacman large according to SIG maintainability?
	No, it is at the bottom of the smallest category following the ranking scheme on page 25
- what is the ratio between actual code and test code size?
	557 lines of test / 1900 LOC = 0.29 lines of test code per LOC

Sanity checks:
- write tests to ensure you are correctly skipping multi-line comments
- and to ensure that consecutive newlines are counted as one.
- compare you results to external tools sloc and/or cloc.pl

./node_modules/.bin/cloc jpacman/src/main/java
      44 text files.
      44 unique files.                              
       0 files ignored.

github.com/AlDanial/cloc v 1.74  T=0.09 s (466.1 files/s, 47068.6 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                            44            555           1987           1901
-------------------------------------------------------------------------------
SUM:                            44            555           1987           1901
-------------------------------------------------------------------------------

Bonus:
- write a hierarchical tree map visualization using vis::Figure and 
  vis::Render quickly see where the large files are. 
  (https://en.wikipedia.org/wiki/Treemapping) 
*/

alias SLOC = map[loc file, int sloc];
SLOC sloc(loc project) {
  SLOC result = ();
  result = (f: countLOC(readFileLines(f))| f <- files(project));
  return result;
}

test bool testSloc()
	= sloc(|project://jpacman-framework/src/syntaxtest/|)
	== (|project://jpacman-framework/src/syntaxtest/LongClass.java|:31,
	 		  |project://jpacman-framework/src/syntaxtest/Main.java|:6);

public int countLOC(list[str] file) {
	int blankLines = countBlankLines(file);
	int commentLines = countComment(file);
	int totalLines = size(file);
	int sLOC = totalLines - blankLines - commentLines;
	return sLOC;
}

test bool testCountLOC()
	= countLOC(["line1","line2","line3","//","/*","* comment body","*/","line4"])
	== 4;

public int countBlankLines(list[str] file){
  n = 0;
  for(s <- file)
    if(/^[ \t\r\n]*$/ := s)  
      n +=1;
  return n;
}
	
test bool testCountBlankLines()
	= countBlankLines(["\n", " \n", " \t\n", "\t\t\t\n", "//comment", "int var = 5"])
	== 4;

public int countComment(list[str] file){	
  n = 0;
  for(s <- file)
  	if(/^(\t|\s)*?(\/\*|\*|\/\/)/ := s)
      n +=1;
  return n;
}

test bool testCountComment()
	= countComment(["/**","* comment body","*/"," ","// test comment", "int var = 5;"])
	== 4;

public int maxLOC(SLOC filelist) {
	return max([filelist[file] | file <- filelist]);
}

test bool testMaxLOC()
	= maxLOC(sloc(|project://jpacman-framework/src/syntaxtest/|))
	== 31;

public int totalLOC(SLOC filelist) {
	return sum([filelist[file] | file <- filelist]);
}

test bool testTotalLOC()
	= totalLOC(sloc(|project://jpacman-framework/src/syntaxtest/|))
	== 37;

