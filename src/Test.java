import solver.*;

public class Test {

	final static String EMCFile = "test/EMC.problem";
	final static String SudokuFile = "test/Sudoku.problem";

	public static void main(String[] args) {
		TestEMC();
		TestSudoku();
	}

	static void TestEMC() {
		Solver sol = new EMC(EMCFile);
		//System.out.println(sol);
		sol.Run();
	}

	static void TestSudoku() {
		Solver sol = new Sudoku(SudokuFile);
		//System.out.println(sol);
		sol.Run();
	}
	
}
