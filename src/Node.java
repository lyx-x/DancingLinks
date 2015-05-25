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

	@Override
	public String toString() {
		return String.valueOf(N);
	}

}
