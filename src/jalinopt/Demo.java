package jalinopt;

import jalinopt.LP.OptimizationType;
import jalinopt.Variable.TYPE;
import jalinopt.cplex.CPLEX_SSH;
import jalinopt.lpsolve.LpSolve;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimize obj: 3x + 2y Subject To 4x + y < 15 5y > 10 General y Binary
 * 
 * End
 * 
 * 
 * 
 * 
 * 
 * @author lhogie
 * 
 */

public class Demo
{

    public static void main(String[] args)
    {
	LP lp = new LP();
	lp.setOptimizationType(OptimizationType.MAX);

	// define the objective: x + y
	LinearExpression objective = lp.getObjective();
	objective.addTerm(1, lp.getVariableByName("x"));

	Constraint c1 = new Constraint();
	c1.getLeftHandSide().addTerm(1, lp.getVariableByName("x"));
	c1.setOperator("<=");
	c1.setRightHandSide(7);
	lp.getConstraints().add(c1);


	// define the type for variable
	lp.getVariableByName("x").setType(TYPE.INTEGER);	
	
	PipedLPSolver.DEBUG = false;

	List<LPSolver> solvers = new ArrayList<LPSolver>();
	// solvers.add(new ApacheSolver());
	// solvers.add(new LocalGLPK());
	solvers.add(new CPLEX_SSH(new SSHConnectionInfo("musclotte")));
	solvers.add(new LpSolve());
//	solvers.add(new Cbc());

	for (LPSolver s : solvers)
	{
	    System.out.println("************************************");
	    System.out.println(s.getClass().getName());
	    System.out.println("************************************");
	    System.out.println(lp.solve(s));
	}
    }
}
