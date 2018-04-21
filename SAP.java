package main;

import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Stack;

public class SAP {
	private final Digraph G;

	/**
	 * constructor takes a digraph (not necessarily a DAG)
	 * @param G - a pre-constructed Digraph
	 * @throws NullPointerException
	 */
	public SAP(Digraph G) {
		if (G == null) throw new NullPointerException("Args cannot be null");
		this.G = G;
	}
	
	/**
	 * is the digraph a directed acyclic graoh?
	 * @return
	 */
	public boolean isDAG() {
		DirectedCycle dc = new DirectedCycle(G);
		return dc.hasCycle();
	}
	
	/**
	 * is the digraph a rooted DAG?
	 * @return
	 */
	public boolean isRootedDAG() {
		int roots = 0;
		for (int i = 0; i < G.V(); i++) {
			if (!G.adj(i).iterator().hasNext()) roots++;
		}
		return roots == 1;
	}
	
	/**
	 * 
	 * @param v
	 * @param w
	 * @return common ancestral path between v and w
	 *         or -1 if no such path.
	 */
	public int length(int v, int w) {
		if (v < 0 || v > G.V() -1) throw new IndexOutOfBoundsException("Vertex v is not between 0 and G.V() -1");
		if (w < 0 || w > G.V() -1) throw new IndexOutOfBoundsException("Vertex w is not between 0 and G.V() -1");
		if (!isRootedDAG()) throw new IllegalArgumentException("Use a rooted DAG");
		int root = 0;
		int L = -1;
		for (int i = 0; i < G.V(); i++) {
			if (!G.adj(i).iterator().hasNext()) root = i;
		}
		Stack<Integer> vStack = (Stack<Integer>)new DepthFirstDirectedPaths(G,v).pathTo(root);
		Stack<Integer> wStack = (Stack<Integer>)new DepthFirstDirectedPaths(G,w).pathTo(root);
		
		while (!vStack.isEmpty()) {
			if (vStack.peek() == wStack.peek()) {
				vStack.pop();
				wStack.pop();
			} else break;
		}
		L = vStack.size() + wStack.size();
		return L;
	}
	
	/**
	 * 
	 * @param v
	 * @param w
	 * @return a common ancestor of v and w that participates in a shortest ancestral path
	 * 		   or -1 if no such path.
	 * @throws IndexOutOfBoundsException
	 */
	public int ancestor(int v, int w) {
		if (v < 0 || v > G.V() -1) throw new IndexOutOfBoundsException("Vertex v is not between 0 and G.V() -1");
		if (w < 0 || w > G.V() -1) throw new IndexOutOfBoundsException("Vertex w is not between 0 and G.V() -1");
		
		int root = 0;
		for (int i = 0; i < G.V(); i++) {
			if (!G.adj(i).iterator().hasNext()) root = i;
		}
		Stack<Integer> vStack = (Stack<Integer>)new DepthFirstDirectedPaths(G,v).pathTo(root);
		Stack<Integer> wStack = (Stack<Integer>)new DepthFirstDirectedPaths(G,w).pathTo(root);
		
		int ca = -1;
		while (!vStack.isEmpty()) {
			if (vStack.peek() == wStack.peek()) {
				ca = vStack.peek();
				vStack.pop();
				wStack.pop();
			} else break;
		}
		return ca;
	}
	
	/**
	 * 
	 * @param v
	 * @param w
	 * @return length of shortst ancestral path between any vertex in v and any vertex in w;
	 *          -1 if no such path.
	 * @throws NullPointerException
	 */
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) throw new NullPointerException("Args cannot be null");
		return 0;
	}
	
	/**
	 * 
	 * @param v
	 * @param w
	 * @return a common ancestor that participates in shortest ancestral path, -1 if no such path.
	 * @throws NullPointerException
	 */
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) throw new NullPointerException("Args cannot be null");
		return 0;
	}
	
	public static void main(String[] args) {
		// Unit testing
	}
	
}
