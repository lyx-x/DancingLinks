package dlx;

import java.util.LinkedList;


public class DLXSearcher {
	
	LinkedList<Node> O;
	Node[] C;
	Node h;
	
	public DLXSearcher(boolean[][] matrix, int row, int column) {
		O = new LinkedList<Node>();
		C = new Node[column];
		Node[] _C = new Node[column];
		h = new Node(0, -1);
		
		Node cursor = h;
		for (int j = 0 ; j < column ; j++) {
			Node tmp = new Node(0, 0);
			C[j] = tmp;
			_C[j] = tmp;
			cursor.R = tmp;
			tmp.L = cursor;
			cursor = tmp;
		}
		cursor.R = h;
		h.L = cursor;
		
		for (int i = 0 ; i < row ; i++) {
			int j;
			boolean found = false;
			Node header = new Node(i + 1, 0);
			for (j = 0 ; j < column ; j++) {
				if (matrix[i][j]) {
					if (!found) {
						found = true;
						cursor = header;
						cursor.S = j;
					}
					else {
						Node tmp = new Node(i + 1, j);
						tmp.L = cursor;
						cursor.R = tmp;
						cursor = tmp;
					}
					cursor.C = C[j];
					C[j].S++;
					cursor.U = _C[j];
					_C[j].D = cursor;
					_C[j] = cursor;
				}
			}
			header.L = cursor;
			cursor.R = header;
		}
		for (int j = 0 ; j < column ; j++) {
			_C[j].D = C[j];
			C[j].U = _C[j];
		}
	}
		
	private void Search(int k) {
		if (h.R == h)
			PrintResult();
		else {
			Node c = ChooseColumn();
			Node.Cover(c);
			Node r = c.D;
			while (r != c) {
				O.add(r);
				Node j = r.R;
				while (j != r) {
					Node.Cover(j.C);
					j = j.R;
				}
				Search(k + 1);
				j = r.L;
				while (j != r) {
					Node.Uncover(j.C);
					j = j.L;
				}
				O.removeLast();
				r = r.D;
			}
			Node.Uncover(c);
		}
	}
	
	private Node ChooseColumn() {
		return h.R;
	}
	
	private void PrintResult() {
		System.out.println("Result:");
		for (Node n : O) {
			System.out.println(n);
		}
	}

	public void Run() {
		Search(0);
	}
	
}
