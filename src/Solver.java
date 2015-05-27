import java.util.LinkedList;

public abstract class Solver {
	
	protected boolean[][] matrix;
	protected int row;
	protected int column;
	protected int secondaries;
	protected LinkedList<LinkedList<Node>> results = new LinkedList<>();

	public void Run() {
		DLXSearcher dlx = new DLXSearcher(matrix, row, column, secondaries);
		dlx.Run();
		System.out.println(dlx.GetResultCount());
		results = dlx.GetResult();
	}

	public void RunDemo() {
		Run();
		ShowResult();
	}

	public void RunTest() {
		Run();
		if (results.size() <= 10)
			PrintAllResult();
		else if (results.size() >= 0)
			PrintOneResult(0);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(row);
		sb.append('\t');
		sb.append(column);
		sb.append('\n');
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++)
				sb.append(matrix[i][j] ? '1' : '0');
			sb.append('\n');
		}
		return sb.toString();
	}

	protected abstract void PrintAllResult(); // different interpretations

	protected abstract void PrintOneResult(int i);

	protected abstract void ShowResult();

}
