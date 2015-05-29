import java.io.*;
import java.util.LinkedList;

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

	@Override
	protected void PrintResult(LinkedList<Node> result) {
		result.forEach(System.out::println);
	}

	@Override
	protected void ShowResult() {

	}

}
