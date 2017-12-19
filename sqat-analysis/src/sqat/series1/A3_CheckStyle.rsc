module sqat::series1::A3_CheckStyle

import Java17ish;
import Message;
import lang::java::jdt::m3::AST;
import IO;
import ParseTree;
import lang::java::m3::Core;
import Type;
import List;
import Message;

/*

Assignment: detect style violations in Java source code.
Select 3 checks out of this list:  http://checkstyle.sourceforge.net/checks.html
Compute a set[Message] (see module Message) containing 
check-style-warnings + location of  the offending source fragment. 

Plus: invent your own style violation or code smell and write a checker.

Note: since concrete matching in Rascal is "modulo Layout", you cannot
do checks of layout or comments (or, at least, this will be very hard).

JPacman has a list of enabled checks in checkstyle.xml.
If you're checking for those, introduce them first to see your implementation
finds them.

Questions
- for each violation: look at the code and describe what is going on? 
  Is it a "valid" violation, or a false positive?

Tips 

- use the grammar in lang::java::\syntax::Java15 to parse source files
  (using parse(#start[CompilationUnit], aLoc), in ParseTree)
  now you can use concrete syntax matching (as in Series 0)

- alternatively: some checks can be based on the M3 ASTs.

- use the functionality defined in util::ResourceMarkers to decorate Java 
  source editors with line decorations to indicate the smell/style violation
  (e.g., addMessageMarkers(set[Message]))

  
Bonus:
- write simple "refactorings" to fix one or more classes of violations 

*/

set[Message] checkStyle() {
  set[Declaration] decls = createAstsFromEclipseProject(|project://jpacman-framework/src/main/java/nl/tudelft/jpacman|, true);  
  return visitDeclarations(decls);
}

set[Message] visitDeclarations(set[Declaration] decls) {
	set[Message] result = {};
	for(decl <- decls) {
		result += checkParameters(decl);
		result += fileLength(decl);
		//result += privateClass(decl);
		//result += customCheck(decl);
	}
	return result;
}
  
set[Message]  fileLength(Declaration decl) {
	set[Message] messages = {};
	totalLength = decl.src.end.line - decl.src.begin.line;
	if (totalLength > 150) {
	messages += warning("The length of this file exceed the length of 150", decl.src);
	}
	return messages;
}

	

set[Message] checkParameters(Declaration decl) {
	set[Message] messages = {};
	visit(decl) {
		case m:\method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	int max_params = 3;
	    	if(size(parameters) > max_params) {
	    		messages += {error("Amount of function parameters exceeds the maximum of 3.", parameters[max_params].src)};
	    	}
	    }
	}
	return messages;
}

set[Message] privateClass(Declaration decl) {
	set[Message] messages = {};
}

/* Styles to check:
 * - Number of parameters does not exceed 7
 * - File length does not exceed 150 lines
 * - Class with only private constructors must be final */
