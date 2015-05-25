import java.util.LinkedList;

public class DLXSearcher {
	
	private LinkedList<Node> O; // save current result
	private Node[] C; // column nodes
	private Node h; // header
	private LinkedList<LinkedList<Node>> results = new LinkedList<>(); // save all possible results
	private int secondaries;
	private int skipped;

	public DLXSearcher(boolean[][] matrix, int row, int column, int _secondaries) {
		secondaries = _secondaries;
		skipped = 0;
		O = new LinkedList<>();
		C = new Node[column];
		Node[] _C = new Node[column]; // last node of every column
		h = new Node(0, -1);
		
		Node cursor = h;
		for (int j = 0; j < column; j++) {
			Node tmp = new Node(0, j); // row and size
			C[j] = tmp;
			_C[j] = tmp; // at first, last node is also the first node
			cursor.R = tmp;
			tmp.L = cursor;
			cursor = tmp;
		}
		cursor.R = h;
		h.L = cursor;
		
		for (int i = 0 ; i < row ; i++) {
			int j;
			boolean found = false;
			Node header = new Node(i + 1, 0); // row and column
			for (j = 0; j < column; j++) {
				if (matrix[i][j]) {
					if (!found) {
						found = true;
						cursor = header;
						cursor.S = j; // set column
					}
					else {
						Node tmp = new Node(i + 1, j);
						tmp.L = cursor;
						cursor.R = tmp;
						cursor = tmp;
					}
					cursor.C = C[j];
					cursor.U = _C[j];
					_C[j].D = cursor;
					_C[j] = cursor;
				}
			}
			if (found) { // if there is nothing in the row, do nothing
				header.L = cursor;
				cursor.R = header;
			}
		}
		for (int j = 0; j < column; j++) {
			_C[j].D = C[j];
			C[j].U = _C[j];
		}
	}

	@SuppressWarnings("unchecked")
	private void Search(int k) {
		if (h.R == h)
			results.add((LinkedList<Node>) O.clone()); // insert a copy of the result
		else {
			Node c = ChooseColumn();
			for (int i = 0; i < 2; i++) {
				if (i == 0) {
					Cover(c);
					Node r = c.D;
					while (r != c) {
						O.add(r);
						Node j = r.R;
						while (j != r) {
							Cover(j.C);
							j = j.R;
						}
						Search(k + 1);
						j = r.L;
						while (j != r) {
							Uncover(j.C);
							j = j.L;
						}
						O.removeLast();
						r = r.D;
					}
					Uncover(c);
				}
				else if (skipped < secondaries) {
					skipped++;
					Cover(c); // cover the column and let this column be 0
					Search(k + 1);
					Uncover(c);
					skipped--;
				}
				else
					break;
			}
		}
	}

	public void Run() {
		Search(0);
	}

	public LinkedList<LinkedList<Node>> GetResult() {
		return results;
	}

	private Node ChooseColumn() {
		return h.R;
	}
	
	private void Cover(Node c) {
		c.R.L = c.L;
		c.L.R = c.R;
		Node r = c.D;
		while (r != c) {
			Node j = r.R;
			while (j != r) {
				j.D.U = j.U;
				j.U.D = j.D;
				j = j.R;
			}
			r = r.D;
		}
	}

	private void Uncover(Node c) {
		Node r = c.U;
		while (r != c) {
			Node j = r.L;
			while (j != r) {
				j.D.U = j;
				j.U.D = j;
				j = j.L;
			}
			r = r.U;
		}
		c.R.L = c;
		c.L.R = c;
	}

	private void PrintStatus() {
		O.forEach(node -> System.err.format("%d ", node.N));
		System.err.println();
	}

}