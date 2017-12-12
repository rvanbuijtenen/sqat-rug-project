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
  result = {<dec.src, 1 + cntDecl(dec)>| dec <- getMethodDecls(decls)};
  for(r <- result) {
  	if(r.cc > 10) {
  		println(r);
  	}
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
	return method_decls;
}

list[Declaration] getMethodDecls(Declaration moduleDecl) {
	method_decls = [];
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


int cntDecl(list[Declaration] decls) {
	return sum([0] + [cntDecl(x) | x <- decls]);
}

int cntDecl(Declaration decl) {
	int cnt = 0;
	visit(decl) {
		case \compilationUnit(list[Declaration] imports, list[Declaration] types): {
			cnt += cntDecl(imports);
			cnt += cntDecl(types);
		}
    	case \compilationUnit(Declaration package, list[Declaration] imports, list[Declaration] types): {
    		cnt += cntDecl(package);
    		cnt += cntDecl(imports);
			cnt += cntDecl(types);
    	}
	    case \enum(str name, list[Type] implements, list[Declaration] constants, list[Declaration] body): {
	    	cnt += cntDecl(constants);
	    	cnt += cntDecl(body);
	    }
	    case \enumConstant(str name, list[Expression] arguments, Declaration class): {
	    	cnt += cntExpr(arguments);
	    	cnt += cntDecl(class);
	    }
	    case \enumConstant(str name, list[Expression] arguments): {
	    	cnt += cntExpr(arguments);
	    }
	    case \class(str name, list[Type] extends, list[Type] implements, list[Declaration] body): {
	    	cnt += cntDecl(body);
	    }
	    case \class(list[Declaration] body): {
	    	cnt += cntDecl(body);
	    }
	    case \interface(str name, list[Type] extends, list[Type] implements, list[Declaration] body): {
	    	cnt += cntDecl(body);
	    }
	    case \field(Type \type, list[Expression] fragments): {
	    	cnt += cntExpr(fragments);
	    }
	    case \initializer(Statement initializerBody): {
	    	cnt += cntStmt(initializerBody);
	    }
	    /*case \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	cnt += cntDecl(parameters);
	    	cnt += cntExpr(exceptions);
	    	cnt += cntStmt(impl);
	    }
	    case \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions): {
	    	cnt += cntDecl(parameters);
	    	cnt += cntExpr(exceptions);
	    }
	    case \constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	cnt += cntDecl(parameters);
	    	cnt += cntExpr(exceptions);
	    	cnt += cntStmt(impl);
	    }*/
	    case \package(Declaration parentPackage, str name): {
	    	cnt += cntDecl(parentPackage);
	    }
	    case \variables(Type \type, list[Expression] \fragments): {
	    	// fragments?
	    	cnt = cnt;
	    }
	    case \annotationType(str name, list[Declaration] body): {
	    	cnt += cntDecl(body);
	    }
	    case \annotationTypeMember(Type \type, str name, Expression defaultBlock): {
	    	cnt += cntExpr(defaultBlock);
	    }
	}
	return cnt;
}

int cntStmt(list[Statement] stmts) {
	return sum([0] + [cntStmt(x) | x <- stmts]); 
}

int cntStmt(Statement stmt) {
	int cnt = 0;
	int return_cnt = -1;
   	visit (stmt) {
   		case \assert(Expression expression): {
   			cnt += cntExpr(expression);
   		}
    	case \assert(Expression expression, Expression message): {
    		cnt += cntExpr(expression);
    		cnt += cntExpr(message);
    	}
    	case \block(list[Statement] statements): {
    		cnt += cntStmt(statements);
    	}
    	case \break(): {
    		cnt += 1;
    	}
    	case \break(str label): {
    		cnt += 1;
    	}
    	case \continue(): {
    		cnt += 1;
    	}
    	case \continue(str label): {
    		cnt +=1;
    	}
    	case \do(Statement body, Expression condition): {
    		cnt += cntStmt(body);
    		cnt += cntExpr(condition);
    	}
    	case \foreach(Declaration parameter, Expression collection, Statement body): {
			// +1 for foreach
			cnt += 1;
			cnt += cntDecl(parameter);
			cnt += cntExpr(collection);
			cnt += cntStmt(body);
    	}
    	case \for(list[Expression] initializers, Expression condition, list[Expression] updaters, Statement body): {
    		// +1 for for
    		cnt += 1;
    		cnt += cntExpr(initializers);
    		cnt += cntExpr(condition);
    		cnt += cntExpr(updaters);
    		cnt += cntStmt(body);
    	}
    	case \for(list[Expression] initializers, list[Expression] updaters, Statement body): {
    		// +1 for for
    		cnt += 1;
    		cnt += cntExpr(initializers);
    		cnt += cntExpr(updaters);
    		cnt += cntStmt(body);
    	}
    	case \if(Expression condition, Statement thenBranch): {
    		// +1 for if
    		cnt += 1;
    		cnt += cntExpr(condition);
    		cnt += cntStmt(thenBranch);
    	}
    	case \if(Expression condition, Statement thenBranch, Statement elseBranch): {
    		// +1 for if, +1 for else
    		cnt += 2;
    		cnt += cntExpr(condition);
    		cnt += cntStmt(thenBranch);
    		cnt += cntStmt(elseBranch);
    	}
    	case \label(str name, Statement body): {
    		cnt += cntStmt(body);
    	}
    	case \return(Expression expression): {
    		cnt += cntExpr(expression);
    		return_cnt += 1;
    	}
    	case \return(): {
    		return_cnt += 1;
    	}
    	case \switch(Expression expression, list[Statement] statements): {
    		cnt += cntExpr(expression);
    		cnt += cntStmt(statements);
    	}
    	case \case(Expression expression): {
			// +1 for case
			cnt += 1;
			cnt += cntExpr(expression);
    	}
    	case \defaultCase(): {
    		// +1 for case
    		cnt += 1;
    	}
    	case \synchronizedStatement(Expression lock, Statement body): {
    		cnt += cntExpr(lock);
    		cnt += cntStmt(body);
    	}
    	case \throw(Expression expression): {
    		// +1 for throw
    		cnt += 1;
    		cnt += cntExpr(expression);
    	}
    	case \try(Statement body, list[Statement] catchClauses): {
    		cnt += cntStmt(body);
    		cnt += cntStmt(catchClauses);
    	}
    	case \try(Statement body, list[Statement] catchClauses, Statement \finally): {
    		// +1 for finally
    		cnt += 1;
    		cnt += cntStmt(body);
    		cnt += cntStmt(catchClauses);
    		// finally?
    	}                                        
    	case \catch(Declaration exception, Statement body): {
    		// +1 for catch
    		cnt += 1;
    		cnt += cntDecl(exception);
    		cnt += cntStmt(body);
    	}
    	case \declarationStatement(Declaration declaration): {
    		cnt += cntDecl(declaration);
    	}
    	case \while(Expression condition, Statement body): {
			// +1 for while
			cnt += 1;
			cnt += cntExpr(condition);
			cnt += cntStmt(body);
    	}
    	case \expressionStatement(Expression stmt): {
    		cnt += cntExpr(stmt);
    	}
    	case \constructorCall(bool isSuper, Expression expr, list[Expression] arguments): {
    		cnt += cntExpr(expr);
    		cnt += cntExpr(arguments);
    	}
    	case \constructorCall(bool isSuper, list[Expression] arguments): {
    		cnt += cntExpr(arguments);
    	}
    }
    return return_cnt > 0 ? cnt + return_cnt : cnt;
}
int cntOperator(str operator) {
	cnt = 0;
	cnt += (operator == "AND"|| operator == "&&") ? 1 : 0;
	cnt += (operator == "OR" || operator == "||") ? 1 : 0;
	cnt += (operator == "?" || operator == ":") ? 1 : 0;
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
	    	cnt += cntDecl(class);
	    }
	    case \newObject(Expression expr, Type \type, list[Expression] args): {
	    	cnt += cntExpr(expr);
	    	cnt + cntExpr(args);
	    }
	    case \newObject(Type \type, list[Expression] args, Declaration class): {
	    	cnt += cntExpr(args);
	    	cnt += cntDecl(class);
	    }
	    case \newObject(Type \type, list[Expression] args): {
	    	cnt += cntExpr(args);
	    }
	    case \qualifiedName(Expression qualifier, Expression expression): {
	    	cnt += cntExpr(qualifier);
	    	cnt += cntExpr(expression);
	    }
	    case \conditional(Expression expression, Expression thenBranch, Expression elseBranch): {
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
	    case \declarationExpression(Declaration decl): {
	    	cnt += cntDecl(decl);
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
