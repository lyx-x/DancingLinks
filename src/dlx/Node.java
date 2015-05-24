package dlx;

public class Node {
	
	public Node L;
	public Node R;
	public Node U;
	public Node D;
	public Node C;
	
	public int N; //name or row
	public int S; //size or column
	
	public Node(int n, int s) {
		L = this;
		R = this;
		U = this;
		D = this;
		C = this;
		S = s;
		N = n;
	}
	
	public static void Cover(Node c) {
		c.R.L = c.L;
		c.L.R = c.R;
		Node r = c.D;
		while (r != c) {
			Node j = r.R;
			while (j != r) {
				j.D.U = j.U;
				j.U.D = j.D;
				j.C.S--;
				j = j.R;
			}
			r = r.D;
		}
	}
	
	public static void Uncover(Node c) {
		Node r = c.U;
		while (r != c) {
			Node j = r.L;
			while (j != r) {
				j.C.S++;
				j.D.U = j;
				j.U.D = j;
				j = j.L;
			}
			r = r.U;
		}
		c.R.L = c;
		c.L.R = c;
	}
	
	@Override
	public String toString() {
		return String.valueOf(N);
	}

}
