import java.util.LinkedList;

public abstract class Solver {
	
	protected boolean[][] matrix;
	protected int row;
	protected int column;
	protected int secondaries;
	protected LinkedList<LinkedList<Node>> results = new LinkedList<>();

	public void Run() {
		long tStart = System.currentTimeMillis();
		DLXSearcher dlx = new DLXSearcher(matrix, row, column, secondaries);
		dlx.Run();
		System.out.println(dlx.GetResultCount());
		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;
		results = dlx.GetResult();
		// Only for test
		/*
		int count = 0;
		for (int i = 0; i < row; i++)
			for (int j = 0; j < column; j++)
				if (matrix[i][j])
					count++;
		System.err.format("Time elapsed: %f s.\n", elapsedSeconds);
		System.err.format("Matrix: %d * %d.\n", row, column);
		System.err.format("%f per row, %f per column.\n", count / (float)row, count / (float)column);
		*/
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

	private void PrintAllResult() {
		System.out.format("%s Results:\n", this.getClass().getName());
		results.forEach(this::PrintResult);
	}

	private void PrintOneResult(int i) {
		System.out.println(String.format("%s Result n°%d:", this.getClass().getName(), i + 1));
		PrintResult(results.get(i));
	}

	protected abstract void PrintResult(LinkedList<Node> result); // different interpretations

	protected abstract void ShowResult();

}
