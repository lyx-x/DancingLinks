import solver.*;

public class Test {
	
	public static void main(String[] args) {
		String EMCFile = "test/EMC.problem";
		Solver sol = new EMC(EMCFile);
		System.out.println(sol);
		sol.Run();
	}
	
}
