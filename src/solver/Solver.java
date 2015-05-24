package solver;

import dlx.DLXSearcher;
import dlx.Node;

import java.util.LinkedList;

public abstract class Solver {
	
	boolean[][] matrix;
	int row;
	int column;
	LinkedList<LinkedList<Node>> results = new LinkedList<>();

	public void Run() {
		DLXSearcher dlx = new DLXSearcher(matrix, row, column);
		dlx.Run();
		results = dlx.GetResult();
		PrintResult();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++)
				sb.append(matrix[i][j] ? '1' : '0');
			sb.append('\n');
		}
		return sb.toString();
	}

	public abstract void PrintResult(); // different interpretations

}
