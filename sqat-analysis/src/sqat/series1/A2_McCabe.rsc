module sqat::series1::A2_McCabe

import lang::java::jdt::m3::AST;
import IO;
import ParseTree;
import lang::java::m3::Core;
import Type;
import List;
import vis::Figure;
import vis::Render;
/*

Construct a distribution of method cylcomatic complexity. 
(that is: a map[int, int] where the key is the McCabe complexity, and the value the frequency it occurs)


Questions:
- which method has the highest complexity (use the @src annotation to get a method's location)

- how does pacman fare w.r.t. the SIG maintainability McCabe thresholds?

- is code size correlated with McCabe in this case (use functions in analysis::statistics::Correlation to find out)? 
  (Background: Davy Landman, Alexander Serebrenik, Eric Bouwers and Jurgen J. Vinju. Empirical analysis 
  of the relationship between CC and SLOC in a large corpus of Java methods 
  and C functions Journal of Software: Evolution and Process. 2016. 
  http://homepages.cwi.nl/~jurgenv/papers/JSEP-2015.pdf)
  
- what if you separate out the test sources?

Tips: 
- the AST data type can be found in module lang::java::m3::AST
- use visit to quickly find methods in Declaration ASTs
- compute McCabe by matching on AST nodes

Sanity checks
- write tests to check your implementation of McCabe

Bonus
- write visualization using vis::Figure and vis::Render to render a histogram.

*/
void main() {
	dists = ccDist(cc(jpacmanASTs()));
	println(dists);
}

//CC cc(set[Declaration] decls) {

//}

// CCDist(CC cc) {
	
//}




set[Declaration] jpacmanASTs() = createAstsFromEclipseProject(|project://jpacman-framework/src/main/java/nl/tudelft/jpacman|, true); 

alias CC = rel[loc method, int cc];

CC cc(set[Declaration] decls) {	
  CC result = {};
  int cnt = 0;
  list[CC] res = [cntDecl(dec) | dec <- decls];
  for(r <- res) {
  	//if(r.cc > 10) {
  		//println(r);
  	//}
  	result += r;
  	cnt += 1;
  }
  return result;
}



/* Returns a list containing all method declarations */
list[Declaration] getMethodDecls(set[Declaration] modules) {
	method_decls = [];
	decls_list = [getMethodDecls(decl) | decl <- modules];	
	for(decl_list <- decls_list) {
		method_decls += decl_list;
	}
	//println(method_decls);
	return method_decls;
}

list[Declaration] getMethodDecls(Declaration moduleDecl) {
	list[Declaration] method_decls = [];
	visit(moduleDecl) {
		case m:\method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	method_decls += m;
	    }
	    case m:\method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions): {
	    	method_decls += m;
	    }
	    case m:\constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	method_decls += m;
	    }
	}
	return method_decls;
}


alias CCDist = map[int cc, int freq];

CCDist ccDist(CC cc) {
	CCDist result = ();
	for(<m, c> <- cc) {
		if(c notin result) {
			result += (c:1);
		} else {
			result[c] += 1;
		}
	}
	return result;
}

CC computeCC(Declaration dec) {
	CC result = {};
	return result;
	//return method_complexity;
}


/*int cntDecl(list[Declaration] decls) {
	return sum([0] + [cntDecl(x) | x <- decls]);
}*/

CC cntDecl(Declaration decl) {
	CC result = {};
	visit(decl) {
	    case m:\method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	cnt = 1;
	    	//cnt += cntDecl(parameters);
	    	//cnt += cntExpr(exceptions);
	    	cnt += cntStmt(impl);
	    	result += <m.src, cnt>;
	    }
	    case m:\constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	cnt = 1;
	    	//cnt += cntDecl(parameters);
	    	cnt += cntExpr(exceptions);
	    	cnt += cntStmt(impl);	    	
	    	result += <m.src, cnt>;
	    }
	}
	return result;
}

int cntStmt(list[Statement] stmts) {
	return sum([0] + [cntStmt(x) | x <- stmts]); 
}

int cntStmt(Statement stmt) {
	int cnt = 0;
	int return_cnt = -1;
   	visit (stmt) {
    	case \foreach(Declaration parameter, Expression collection, Statement body): {
			// +1 for foreach
			cnt += 1;
    	}
    	case \for(list[Expression] initializers, Expression condition, list[Expression] updaters, Statement body): {
    		// +1 for for
    		cnt += 1;
    	}
    	case \for(list[Expression] initializers, list[Expression] updaters, Statement body): {
    		// +1 for for
    		cnt += 1;
    	}
    	case \if(Expression condition, Statement thenBranch): {
    		// +1 for if
    		cnt += 1;
    	}
    	case \if(Expression condition, Statement thenBranch, Statement elseBranch): {
    		// +1 for if, +1 for else
    		cnt += 1;
    	}
    	case \case(Expression expression): {
			// +1 for case
			cnt += 1;
    	}
    	case \defaultCase(): {
    		cnt += 1;
    	}
    	case \while(Expression condition, Statement body): {
			// +1 for while
			cnt += 1;
    	}
		case \conditional(Expression expression, Expression thenBranch, Expression elseBranch): {
	    	cnt += 1;
	    }
		case \infix(Expression lhs, str operator, Expression rhs): {
	    	cnt += cntOperator(operator);
	    }
	    case \postfix(Expression operand, str operator): {
	    	cnt += cntOperator(operator);
	    }
	    case \prefix(str operator, Expression operand): {
	    	cnt += cntOperator(operator);
	    }
	    case \catch(Declaration exception, Statement body): {
	    	cnt += 1;
	    }
    }
    return cnt;
}

int cntOperator(str operator) {
	cnt = 0;
	cnt += (operator == "&&") ? 1 : 0;
	cnt += (operator == "||") ? 1 : 0;
	return cnt;
}

int cntExpr(list[Expression] exprs) {
	return sum([0] + [cntExpr(x) | x <- exprs]);
}

int cntExpr(Expression expr) {
	cnt = 0;
	visit(expr) {
		case \arrayAccess(Expression array, Expression index): {
			cnt += cntExpr(array);
			cnt += cntExpr(index);
		}
	    case \newArray(Type \type, list[Expression] dimensions, Expression init): {
	    	cnt += cntExpr(dimensions);
	    	cnt += cntExpr(init);
	    }
	    case \newArray(Type \type, list[Expression] dimensions): {
	    	cnt += cntExpr(dimensions);
	    }
	    case \arrayInitializer(list[Expression] elements): {
	    	cnt += cntExpr(elements);
	    }
	    case \assignment(Expression lhs, str operator, Expression rhs): {
	    	cnt += cntOperator(operator);
	    	cnt += cntExpr(rhs);
	    }
	    case \cast(Type \type, Expression expression): {
	    	cnt += cntExpr(expression);
	    }
	    case \newObject(Expression expr, Type \type, list[Expression] args, Declaration class): {
	    	cnt += cntExpr(expr);
	    	cnt += cntExpr(args);
	    	//cnt += cntDecl(class);
	    }
	    case \newObject(Expression expr, Type \type, list[Expression] args): {
	    	cnt += cntExpr(expr);
	    	cnt + cntExpr(args);
	    }
	    case \newObject(Type \type, list[Expression] args, Declaration class): {
	    	cnt += cntExpr(args);
	    	//cnt += cntDecl(class);
	    }
	    case \newObject(Type \type, list[Expression] args): {
	    	cnt += cntExpr(args);
	    }
	    case \qualifiedName(Expression qualifier, Expression expression): {
	    	cnt += cntExpr(qualifier);
	    	cnt += cntExpr(expression);
	    }
	    case \conditional(Expression expression, Expression thenBranch, Expression elseBranch): {
	    	// ? :
	    	cnt += cntExpr(expression);
	    	cnt += cntExpr(thenBranch);
	    	cnt += cntExpr(elseBranch);
	    }
	    case \fieldAccess(bool isSuper, Expression expression, str name): {
	    	cnt += cntExpr(expression);
	    }
	    case \instanceof(Expression leftSide, Type rightSide): {
	    	cnt += cntExpr(leftSide);
	    }
	    case \methodCall(bool isSuper, str name, list[Expression] arguments): {
	    	cnt += cntExpr(arguments);
	    }
	    case \methodCall(bool isSuper, Expression receiver, str name, list[Expression] arguments): {
	    	cnt += cntExpr(receiver);
	    	cnt += cntExpr(arguments);
	    }
	    case \bracket(Expression expression): {
	    	cnt += cntExpr(expression);
	    }
	    case \this(Expression thisExpression): {
	    	cnt += cntExpr(thisExpression);
	    }
	    case \infix(Expression lhs, str operator, Expression rhs): {
	    	cnt += cntExpr(lhs);
	    	cnt += cntOperator(operator);
	    	cnt += cntExpr(rhs);
	    }
	    case \postfix(Expression operand, str operator): {
	    	cnt += cntExpr(operand);
	    	cnt += cntOperator(operator);
	    }
	    case \prefix(str operator, Expression operand): {
	    	cnt += cntOperator(operator);
	    	cnt += cntExpr(operand);
	    }
	    case \normalAnnotation(str typeName, list[Expression] memberValuePairs): {
	    	cnt += cntExpr(memberValuePairs);
	    }
	}
	return cnt;
}
