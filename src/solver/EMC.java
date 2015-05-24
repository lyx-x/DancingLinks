package solver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EMC extends Solver {
	
	public EMC(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				String size[] = reader.readLine().split(" ");
				row = Integer.parseInt(size[0]);
				column = Integer.parseInt(size[1]);
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
