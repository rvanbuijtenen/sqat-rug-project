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
SLOC sloc(loc project, bool isTest) {
	if(!isTest) {
		loc src = project + "/src/main/java/nl/tudelft/jpacman";
		loc testSrc = project + "src/test/java/nl/tudelft/jpacman";
		SLOC result = ();
		SLOC testResult = ();
		result = (f: countLOC(readFileLines(f))| f <- files(src));
	 	testResult = (f: countLOC(readFileLines(f))| f <- files(testSrc));
		answerQuestions(result, testResult);
		return result;
	}
	return (f: countLOC(readFileLines(f))| f <- files(project));
}

void answerQuestions(SLOC result, SLOC testResult) {
	<filename, length> = maxLOC(result);
	println("The largest file in JPacman is: <filename> with a length of <length> lines\n");
	
	int total = totalLOC(result);
	println("The total number of lines in JPacman is: <total>\n");

	println("Is JPacman large according to the SIG maintainbility index?");
	println("No, it is at the bottom of the smallest category following the ranking scheme on page 25\n");
	println("what is the ratio between actual code and test code size?");
	int testTotal = totalLOC(testResult);
	println("The test code size is: <testTotal>");
	real ratio = testTotal*1.0/total;
	println("This results in a ratio of <testTotal>/<total> = <ratio>");
}

test bool testSloc()
	= sloc(|project://jpacman-framework/src/syntaxtest/|, true)
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
  bool isBlockComment = false;
  for(s <- file) {
  	if(isBlockComment) {
		if(/^(\t|\s)*?(\*\/)/ := s) {
  			isBlockComment = false;
  		}
  		n += 1;
  	}
  	if(/^(\t|\s)*?(\/\*)/ := s) {
  		if(!/\*\// := s) {
  			isBlockComment = true;
  		}
  		n+= 1;
  	}
  	if(/^(\t|\s)*?(\/\/)/ := s) {
      n +=1;
    }
  }
  return n;
}

test bool testCountComment()
	= countComment(["/**","* comment body","*/","/**","comment body","*/"," ","// test comment", "int var = 5;"])
	== 7;

tuple[loc, int] maxLOC(SLOC filelist) {
	int max_size = 0;
	loc f;
	for(file <- filelist) {
		if(filelist[file] > max_size) {
			max_size = filelist[file];
			f = file;
		}
	}
	return <f, max_size>;
}

test bool testMaxLOC()
	= maxLOC(sloc(|project://jpacman-framework/src/syntaxtest/|, true))
	== <|project://jpacman-framework/src/syntaxtest/LongClass.java|, 31>;

public int totalLOC(SLOC filelist) {
	return sum([filelist[file] | file <- filelist]);
}

test bool testTotalLOC()
	= totalLOC(sloc(|project://jpacman-framework/src/syntaxtest/|, true))
	== 37;

