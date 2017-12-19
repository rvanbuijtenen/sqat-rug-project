module sqat::series1::A3_CheckStyle

import Java17ish;
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
  set[Message] result = {};
  set[Declaration] decls = createAstsFromEclipseProject(|project://jpacman-framework/src/main/java/nl/tudelft/jpacman|, true); 
  // to be done
  // implement each check in a separate function called here. 
  
  
  return result;
}

  
  set[Message]  methodLength(Declaration decl) {
  visit(decl) {
	    case m:\method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	println(m.impl);
	    	//<property name="max" value="30"/>
	    	
	    	
	    	
	    }
	    }
  
  }





/* Styles to check:
 * - Number of parameters does not exceed 7
 * - Method length does not exceed 30 lines
 * - Class with only private constructors must be final */
