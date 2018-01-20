module sqat::series2::A2_CheckArch

import sqat::series2::Dicto;
import lang::java::jdt::m3::Core;
import Message;
import ParseTree;
import IO;


/*

This assignment has two parts:
- write a dicto file (see example.dicto for an example)
  containing 3 or more architectural rules for Pacman
  
- write an evaluator for the Dicto language that checks for
  violations of these rules. 

Part 1  

An example is: ensure that the game logic component does not 
depend on the GUI subsystem. Another example could relate to
the proper use of factories.   

Make sure that at least one of them is violated (perhaps by
first introducing the violation).

Explain why your rule encodes "good" design.
  
Part 2:  
 
Complete the body of this function to check a Dicto rule
against the information on the M3 model (which will come
from the pacman project). 

A simple way to get started is to pattern match on variants
of the rules, like so:

switch (rule) {
  case (Rule)`<Entity e1> cannot depend <Entity e2>`: ...
  case (Rule)`<Entity e1> must invoke <Entity e2>`: ...
  ....
}

Implement each specific check for each case in a separate function.
If there's a violation, produce an error in the `msgs` set.  
Later on you can factor out commonality between rules if needed.

The messages you produce will be automatically marked in the Java
file editors of Eclipse (see Plugin.rsc for how it works).

Tip:
- for info on M3 see series2/A1a_StatCov.rsc.

Questions
- how would you test your evaluator of Dicto rules? (sketch a design)
- come up with 3 rule types that are not currently supported by this version
  of Dicto (and explain why you'd need them). 
*/


set[Message] eval(start[Dicto] dicto, M3 m3) = eval(dicto.top, m3);

set[Message] eval((Dicto)`<Rule* rules>`, M3 m3) 
  = ( {} | it + eval(r, m3) | r <- rules );
  
set[Message] eval(Rule rule, M3 m3) {
  set[Message] msgs = {};
	  switch(rule) {
	  case (Rule)`<Entity e1> cannot import <Entity e2>`: {
		if(!validateCannotImport(e1, e2, m3)) {
			msgs.add("test");
		}
	  }
	  case (Rule)`<Entity e1> cannot inherit <Entity e2>`: {
		if(!validateCannotInherit(e1, e2, m3)) {
			msgs.add("test2");
		}
	  }
	  case (Rule)`<Entity e1> must inherit <Entity e2>`: {
	  	if(!validateMustInherit(e1, e2)) {
	  		msgs.add("test3");
	  	}
	  }
  }
  // to be done
  
  return msgs;
}

bool validateCannotImport(Entity e1, Entity e2, M3 m3) {
	println(e1);
	println(e2);
	println(m3);
	return true;
}



bool validateCannotInherit(Entity e1, Entity e2, M3 m3) {
	// class cannot inherit class
	return true;
}

bool validateMustInherit(Entity e1, Entity e2, M3 m3) {
	// class must inherit class
	return true;
}



