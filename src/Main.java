public class Main {

	final static String EMCFile = "tests/EMC.problem";
	final static String SudokuFile = "tests/Sudoku.problem";
	final static String PavageFile = "tests/Pavage.problem";

	public static void main(String[] args) {
		switch (args.length) {
			case 0:
				System.out.println("Default Test:");
				new EMC(EMCFile).Run();
				new Sudoku(SudokuFile).Run();
				break;
			case 1:
				switch (args[0]) {
					case "emc":
						System.out.println("EMC Test: Please enter the problem");
						new EMC().Run();
						break;
					case "sudoku":
						System.out.println("Sudoku Test: Please enter the problem");
						new Sudoku().Run();
						break;
					case "pavage":
						System.out.println("Pavage Test: Please enter the problem");
						break;
					default:
						System.out.println("Wrong argument!");
				}
				break;
			default:
				System.out.println("Wrong argument count!");
		}
	}

}
