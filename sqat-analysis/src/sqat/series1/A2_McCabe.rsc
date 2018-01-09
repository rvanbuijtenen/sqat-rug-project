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

  The highest complexity is 8. Following the SIG, the CC of 1-10 has a risk evaluation of 'simple, without much risk'.

- is code size correlated with McCabe in this case (use functions in analysis::statistics::Correlation to find out)? 
  (Background: Davy Landman, Alexander Serebrenik, Eric Bouwers and Jurgen J. Vinju. Empirical analysis 
  of the relationship between CC and SLOC in a large corpus of Java methods 
  and C functions Journal of Software: Evolution and Process. 2016. 
  http://homepages.cwi.nl/~jurgenv/papers/JSEP-2015.pdf)
  
- what if you separate out the test sources?
If you

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

set[Declaration] jpacmanASTs() = createAstsFromEclipseProject(|project://jpacman-framework/src/main/java/nl/tudelft/jpacman|, true); 

alias CC = rel[loc method, int cc];

// cc = 1 + for ---> 2 test cases
CC cc(set[Declaration] decls) {	
  CC result = {};
  int cnt = 0;
  list[CC] res = [cntDecl(dec) | dec <- decls];
  for(r <- res) {
  	result += r;
  	cnt += 1;
  }
  return result;
}

test bool testCcNone()
	= cc({}) == {};

test bool testCcMany()
	= cc({
		createAstFromString(|file://Test1.java|, "class Test1() {public int test() {}}", true),
		createAstFromString(|file://Test2.java|, "class Test2() {public int test() {}}", true),
		createAstFromString(|file://Test3.java|, "class Test3() {public int test() {}}", true)		
	}) == {<|file://Test3.java|(15,20,<1,15>,<1,35>),1>,<|file://Test2.java|(15,20,<1,15>,<1,35>),1>,<|file://Test1.java|(15,20,<1,15>,<1,35>),1>};


alias CCDist = map[int cc, int freq];

// cc = 1 + for + ifelse ---> 3 test cases
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

test bool testCcDistNone()
	= ccDist({}) == ();
	
test bool testCcDistOne()
	= ccDist({<|file://Test.java|,1>}) == (1:1);

test bool testCcDistMany()
	= ccDist({<|file://Test.java|,1>, <|file://Test2.java|,1>}) == (1:2);

// cc = 1 + 2x case ---> 3 test cases
CC cntDecl(Declaration decl) {
	CC result = {};
	visit(decl) {
	    case m:\method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	cnt = 1;
	    	cnt += cntStmt(impl);
	    	println(m.src);
	    	result += <m.src, cnt>;
	    }
	    case m:\constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	cnt = 1;
	    	cnt += cntStmt(impl);	    	
	    	result += <m.src, cnt>;
	    }
	}
	return result;
}

test bool testCntDeclNone()
	= cntDecl(createAstFromString(|file://Test.java|, "class Test{}", true)) == {};

test bool testCntDeclMethod()
	= cntDecl(createAstFromString(|file://Test.java|, "class Test {public void test() {}}", true)) == {<|file://Test.java|(12,21,<1,12>,<1,33>),1>};

test bool testCntDeclConstructor()
	= cntDecl(createAstFromString(|file://Test.java|, "class Test {public Test() {}}", true)) == {<|file://Test.java|(12,16,<1,12>,<1,28>),1>};

// cc = 1 + 13x case ---> 14 test cases
int cntStmt(Statement stmt) {
	int cnt = 0;
   	visit (stmt) {
    	case \foreach(Declaration parameter, Expression collection, Statement body): {
			// +1 for foreach
			cnt += 1;
    	}
    	case \for(list[Expression] initializers, Expression condition, list[Expression] updaters, Statement body): {
    		// +1 for for
    		println("for1");
    		cnt += 1;
    	}
    	case \for(list[Expression] initializers, list[Expression] updaters, Statement body): {
    		// +1 for for
    		println("for2");
    		cnt += 1;
    	}
    	case \if(Expression condition, Statement thenBranch): {
    		// +1 for if
    		println("ifthen");
    		cnt += 1;
    	}
    	case \if(Expression condition, Statement thenBranch, Statement elseBranch): {
    		// +1 for if, +1 for else
    		println("ifthenelse");
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

bool testCntStmtBase(str body, int expected_cnt) {
	input = "class test { public int main(String [] argv {"+body+"}}";
	Declaration d = createAstFromString(|file://Test.java|, input, true);
	cnt = 0;
	visit(d) {
	    case m:\method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl): {
	    	cnt += cntStmt(impl);
	    }
	}
	return cnt == expected_cnt;
}

test bool testCntStmtNone()
	= testCntStmtBase("", 0);

test bool testCntStmtForeach()
	= testCntStmtBase("
		int[] a = {0,1,2,3};
		for(int i : a) {}
		",1);

test bool testCntStmtForA()
	= testCntStmtBase("for(int i;i\<42;i++){}", 1);
	
test bool testCntStmtForB()
	= testCntStmtBase("for(;;){}", 1);
	
test bool testCntStmtIfThen()
	= testCntStmtBase("if(true){}", 1);

test bool testCntStmtIfThenElse()
	= testCntStmtBase("if(true){}else{}", 1);
	
test bool testCntStmtCase()
	= testCntStmtBase("
		int x=1; 
		switch(x) {
			case 1: {
				x = 2;
				break;
			}
		}", 1);
		
test bool testCntStmtDefault()
	= testCntStmtBase("
		int x=1; 
		switch(x) {
			default: {
				x = 2;
				break;
			}
		}", 1);
		
test bool testCntStmtWhile()
	= testCntStmtBase("while(true) {}",1);
	
test bool testCntStmtConditional()
	= testCntStmtBase("
		int x = 1;
		int y = x == 1 ? 0 : 1;", 1);

test bool testCntStmtInfix()
	= testCntStmtBase("if(true && true){}", 2);
	
test bool testCntStmtPrefix()
	= false;
	
test bool testCntStmtPostfix()
	= false;
	
test bool testCntStmtCatch()
	= testCntStmtBase("try{} catch(Exception e) {}", 1);

// cc = 1 + condStmt + condOp ---> 3 test cases
int cntOperator(str operator) {
	return (operator == "&&" || operator == "||") ? 1 : 0;
}

test bool testCntOperatorNone()
	= cntOperator("not a conditional") == 0;

test bool testCntOperatorAnd()
	= cntOperator("&&") == 1;

test bool testCntOperatorOr()
	= cntOperator("||") == 1;

