import java.io.FileNotFoundException;

public class Main {

	final static String EMCFile = "tests/emc/sujet.txt";
	final static String SudokuFile = "tests/sudoku/sudoku16_0.txt";
	final static String PavageFile = "tests/pavage/scott.txt";
	final static String QueensFile = "tests/queens/queens8.txt";

	public static void main(String[] args) {
		switch (args.length) {
			case 0:
				try {
					System.out.println("Default Test:");
					System.out.println(EMCFile);
					new EMC(EMCFile).RunTest();
					System.out.println(PavageFile);
					new Pavage(PavageFile).RunTest();
					System.out.println(SudokuFile);
					new Sudoku(SudokuFile).RunTest();
					System.out.println(QueensFile);
					new Queens(QueensFile).RunTest();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				switch (args[0]) {
					case "emc":
						System.err.println("EMC Test: Please enter the problem");
						new EMC().Run();
						break;
					case "sudoku":
						System.err.println("Sudoku Test: Please enter the problem");
						new Sudoku().Run();
						break;
					case "pavage":
						System.err.println("Pavage Test: Please enter the problem");
						new Pavage().Run();
						break;
					case "queens":
						System.err.println("Queens Test: Please enter the chessboard's dimension");
						new Queens().Run();
						break;
					case "demo":
						try {
							System.out.println("Demo:");
							System.out.println(SudokuFile);
							new Sudoku(SudokuFile).RunDemo();
							System.out.println(PavageFile);
							new Pavage(PavageFile).RunDemo();
							System.out.println(QueensFile);
							new Queens(QueensFile).RunDemo();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						break;
					default:
						System.err.println("Wrong argument!");
				}
				break;
			default:
				System.err.println("Wrong argument count!");
		}
	}

}
