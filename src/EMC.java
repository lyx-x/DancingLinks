import java.io.*;

public class EMC extends Solver {

	public EMC() {
		this(new InputStreamReader(System.in));
	}

	public EMC(String file) throws FileNotFoundException {
		this(new FileReader(file));
	}

	public EMC(Reader stream) {
		BufferedReader reader = new BufferedReader(stream);
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
	}

	public EMC(int _row, int _column, boolean[][] _matrix, int _secondaries) {
		row = _row;
		column = _column;
		matrix = _matrix.clone();
		secondaries = _secondaries;
	}

	@Override
	public void PrintAllResult() {
		results.forEach(result -> {
			System.out.println("EMC Result:");
			result.forEach(System.out::println);
		});
		System.out.println();
	}

	@Override
	protected void ShowOneResult(int i) {

	}

}
