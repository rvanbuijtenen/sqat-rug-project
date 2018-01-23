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
  
 We will take the first style violation of each type that our program reports
 will discuss them below:
 
 Violation 1:
 
	warning(
	    "If there are more than 3 parameters, each parameter should be on a separate line",
	    |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/sprite/EmptySprite.java|(310,10,<16,18>,<16,28>))
	    
	 
	The first violation is from our custom style violation: methods with more than three
	three parameters should list each parameter on a separate line. The method has 5 parameters which
	are all listed on the same line.
 
 	When we fix the violation our program doesn't report it as a violation. The method looks like this:
 	@Override
	public void draw(Graphics g,
					 int x,
					 int y,
					 int width,
					 int height) {

Violation 2:
	warning(
	    "The length of this file exceed the length of 150",
	    |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java|(0,6954,<1,0>,<262,2>))

	This violation simply indicates that the CollisionInteractionMap (one of the more complex files in the Jpacman program)
	is to long according to our current settings. We have deliberately set a very low limit of 150 lines to ensure that some
	files show up for testing purposes. When we increase this limit to 300 for example, our program does not report it anymore
	
Violation 3:
	 error(
	    "The amount of function parameters exceeds the maximum of 4.",
	    |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/level/MapParser.java|(2169,16,<79,20>,<79,36>))
	    
	 This check is a variation of the settings used by the jpacman application. In their configuration file a limit of 7
	 parameters is set, so we decided on a limit of 4 because otherwise no violations would show up. Of course, four parameters
	 is very little and would be to strict for a real application, but it works well for testing purposes. When we set our
	 limit to that of jpacman's configuration our program does not find any violations.
	 
Violation 4:
	warning(
	    "A method that only has private constructors should be declared using the FINAL modifier",
	    |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java|(174,6779,<10,0>,<262,1>))
	    
	This violation was not one that was present in the original version of JPacman, so we changed the constructors
	of a few classes from public to private. As a result, our program is able to detect these violations, even for
	nested class definitions. To test this we changed the constructors of the CollsionInteractionMap.java to private.
	We did the same for the nested private class that is defined within this class. Both violations show up in our output.
	


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
  set[Declaration] decls = createAstsFromEclipseProject(|project://jpacman-style-violations/src/main/java/nl/tudelft/jpacman|, true);  
  return visitDeclarations(decls);
}

void answerQuestions() {
	println("for each violation: look at the code and describe what is going on? Is it a \"valid\" violation, or a false positive?");
  
 	println("\nWe will take the first style violation of each type that our program reports
 will discuss them below:");
 
 println("
 Violation 1:\n warning(
	    \"If there are more than 3 parameters, each parameter should be on a separate line\",\n|project://jpacman-framework/src/main/java/nl/tudelft/jpacman/sprite/EmptySprite.java|(310,10,\<16,18\>,\<16,28\>))");
	    
 println("	 
	The first violation is from our custom style violation: methods with more than three
	three parameters should list each parameter on a separate line. The method has 5 parameters which
	are all listed on the same line.\n
 
 	When we fix the violation our program doesn\'t report it as a violation. The method looks like this:
 	@Override
	public void draw(Graphics g,
					 int x,
					 int y,
					 int width,
					 int height) {");
println("
Violation 2:\n
	warning(
	    \"The length of this file exceed the length of 150\",
	    |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java|(0,6954,\<1,0\>,\<262,2\>))");
println("
	This violation simply indicates that the CollisionInteractionMap (one of the more complex files in the Jpacman program)
	is to long according to our current settings. We have deliberately set a very low limit of 150 lines to ensure that some
	files show up for testing purposes. When we increase this limit to 300 for example, our program does not report it anymore");
	
println("
Violation 3:\n
	 error(
	    \"The amount of function parameters exceeds the maximum of 4.\",
	    |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/level/MapParser.java|(2169,16,\<79,20\>,\<79,36\>))\n
	    
	 This check is a variation of the settings used by the jpacman application. In their configuration file a limit of 7
	 parameters is set, so we decided on a limit of 4 because otherwise no violations would show up. Of course, four parameters
	 is very little and would be to strict for a real application, but it works well for testing purposes. When we set our
	 limit to that of jpacman\'s configuration our program does not find any violations.");
	 
println("
Violation 4:\n
	warning(
	    \"A method that only has private constructors should be declared using the FINAL modifier\",
	    |project://jpacman-framework/src/main/java/nl/tudelft/jpacman/level/CollisionInteractionMap.java|(174,6779,\<10,0\>,\<262,1\>))\n
	    
	This violation was not one that was present in the original version of JPacman, so we changed the constructors
	of a few classes from public to private. As a result, our program is able to detect these violations, even for
	nested class definitions. To test this we changed the constructors of the CollsionInteractionMap.java to private.
	We did the same for the nested private class that is defined within this class. Both violations show up in our output.");
	
}

set[Message] visitDeclarations(set[Declaration] decls) {
	set[Message] result = {};
	for(decl <- decls) {
		result += checkParameters(decl);
		result += fileLength(decl);
		result += privateClass(decl);
		result += methodParameterLineBreaks(decl);

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
	int max_params = 4;
	visit(decl) {
		case \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	if(size(parameters) > max_params) {
	    		messages += {error("The amount of function parameters exceeds the maximum of 4.", parameters[max_params].src)};
	    	}
	    }
	    case \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions): {
	    	if(size(parameters) > max_params) {
	    		messages += {error("The amount of function parameters exceeds the maximum of 4.", parameters[max_params].src)};
	    	}
	    }
	    case \constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl):{
	    	if(size(parameters) > max_params) {
	    		messages += {error("The amount of function parameters exceeds the maximum of 4.", parameters[max_params].src)};
	    	}
	    }
	}
	return messages;
}

set[Message] privateClass(Declaration decl) {
	set[Message] messages = {};
	visit(decl) {
		case c:\class(str name, list[Type] extends, list[Type] implements, list[Declaration] body):{
			if(onlyPrivateConstructors(body)) {
				if(final() notin c.modifiers) {
					messages += {warning("A class that only has private constructors should be declared using the FINAL modifier", c.src)};
				}
			}		
		}
	}
	return messages;
}

bool onlyPrivateConstructors(list[Declaration] body) {
	bool isEmpty = true;
	for(decl <- body) {
		top-down-break visit(decl) {
		    case c:\constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl):{
		    	isEmpty = false;
		    	if(\private() notin c.modifiers) {
		    		return false;
		    	}
		    }
		    case c:\class(str name, list[Type] extends, list[Type] implements, list[Declaration] body): ;
		}
	}
	return !isEmpty;
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
 * - Class with only private constructors must be final
 * - If a method has more than 4 parameters, then these
 *   parameters should each be listed on a separate line */
