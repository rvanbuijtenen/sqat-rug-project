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
		//result += checkParameters(decl);
		//result += fileLength(decl);
		result += privateClass(decl);
		//result += methodParameterLineBreaks(decl);

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
	int max_params = 7;
	visit(decl) {
		case \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	if(size(parameters) > max_params) {
	    		messages += {error("The amount of function parameters exceeds the maximum of 7.", parameters[max_params].src)};
	    	}
	    }
	    case \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions): {
	    	if(size(parameters) > max_params) {
	    		messages += {error("The amount of function parameters exceeds the maximum of 7.", parameters[max_params].src)};
	    	}
	    }
	    case \constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl):{
	    	if(size(parameters) > max_params) {
	    		messages += {error("The amount of function parameters exceeds the maximum of 7.", parameters[max_params].src)};
	    	}
	    }
	}
	return messages;
}

set[Message] privateClass(Declaration decl) {
	set[Message] messages = {};
	visit(decl) {
		case c:\class(str name, list[Type] extends, list[Type] implements, list[Declaration] body):{
			/*if(onlyPrivateConstructors(body)) {
			
			}*/
			if(onlyPrivateConstructors(body)) {
				if(final() notin c.modifiers) {
					messages += {warning("A method that only has private constructors should be declared using the FINAL modifier", c.src)};
				}
			}
			
		}
	}
	return messages;
}

bool onlyPrivateConstructors(list[Declaration] body) {
	bool onlyPrivate = true;
	bool isEmpty = true;
	for(decl <- body) {
		visit(decl) {
		    case c:\constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl):{
		    	isEmpty = false;
		    	if(\private() notin c.modifiers) {
		    		onlyPrivate = false;
		    	}
		    }
		}
	}
	return onlyPrivate && !isEmpty;
}

set[Message] methodParameterLineBreaks(Declaration decl) {
	set[Message] messages = {};
	visit(decl) {
		case \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	messages += checkParameterStyle(parameters);
	    }
	    case \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions): {
	    	messages += checkParameterStyle(parameters);
	    }
	    case \constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl):{
	    	messages += checkParameterStyle(parameters);
	    }
	}
	return messages;
}

set[Message] checkParameterStyle(list[Declaration] parameters) {
	if(size(parameters) <= 3) {
		return {};
	}
	list[int] param_lines = [ p.src.begin.line | p <- parameters ];
	int previous = param_lines[0] - 1;
	for(l <- param_lines) {
		if(l - 1 != previous) {
			return {warning("If there are more than 3 parameters, each parameter should be on a separate line", parameters[0].src)};
		}
		previous = l;
	}
	return {};
}

/* Styles to check:
 * - Number of parameters does not exceed 7
 * - File length does not exceed 150 lines
 * - Class with only private constructors must be final */
