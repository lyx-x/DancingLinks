import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class EMC extends Solver {

	public EMC() {
		Scanner in = new Scanner(System.in);
		column = in.nextInt();
		secondaries = in.nextInt();
		row = in.nextInt();
		column += secondaries;
		matrix = new boolean[row][column];
		in.nextLine(); // nextInt does not finish the line
		for (int i = 0; i < row; i++) {
			String tmp = in.nextLine();
			for (int j = 0; j < column; j++) {
				matrix[i][j] = (tmp.charAt(j) == '1');
			}
		}
	}

	public EMC(int _row, int _column, boolean[][] _matrix, int _secondaries) {
		row = _row;
		column = _column;
		matrix = _matrix.clone();
		secondaries = _secondaries;
	}

	public EMC(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				column = Integer.parseInt(reader.readLine());
				secondaries = Integer.parseInt(reader.readLine());
				row = Integer.parseInt(reader.readLine());
				column += secondaries;
				matrix = new boolean[row][column];
				for (int i = 0; i < row; i++) {
					String tmp = reader.readLine();
					for (int j = 0; j < column; j++) {
						matrix[i][j] = (tmp.charAt(j) == '1');
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void PrintResult() {
		results.forEach(result -> {
			System.out.println("EMC Result:");
			result.forEach(System.out::println);
		});
		System.out.println();
	}

}
