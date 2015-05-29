import java.io.FileNotFoundException;

public class Main {

	final static String EMCFile = "tests/emc/queens8.txt";
	final static String SudokuFile = "tests/sudoku/sudoku16_0.txt";
	final static String PavageFile = "tests/pavage/scott.txt";

	public static void main(String[] args) {
		switch (args.length) {
			case 0:
				try {
					System.out.println("Default Test:");
					System.out.println(EMCFile);
					new EMC(EMCFile).RunTest();
					System.out.println(SudokuFile);
					new Sudoku(SudokuFile).RunTest();
					System.out.println(PavageFile);
					new Pavage(PavageFile).RunTest();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
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
						new Pavage().Run();
						break;
					case "demo":
						try {
							System.out.println("Default Test:");
							System.out.println(SudokuFile);
							new Sudoku(SudokuFile).RunDemo();
							System.out.println(PavageFile);
							new Pavage(PavageFile).RunDemo();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
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
