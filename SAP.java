package main;

import java.util.ArrayList;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
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
		throwOOB(v, w);
		if (!isRootedDAG()) throw new IllegalArgumentException("Use a rooted DAG");
		
		int root = 0;
		int L = -1;
		for (int i = 0; i < G.V(); i++) {
			if (!G.adj(i).iterator().hasNext()) root = i;
		}
		Stack<Integer> vStack = (Stack<Integer>)new BreadthFirstDirectedPaths(G,v).pathTo(root);
		Stack<Integer> wStack = (Stack<Integer>)new BreadthFirstDirectedPaths(G,w).pathTo(root);
		Stack<Integer> vStack2 = new Stack<Integer>();
		for (int i = 0; i < vStack.size(); i++) {
			vStack2.push(vStack.pop());
		}
		Stack<Integer> wStack2 = new Stack<Integer>();
		for (int i = 0; i < wStack.size(); i++) {
			wStack2.push(wStack.pop());
		}
		
		while (!vStack2.isEmpty()) {
			if (vStack2.peek().equals(wStack2.peek())) {
				vStack2.pop();
				wStack2.pop();
			} else break;
		}
		L = vStack2.size() + wStack2.size();
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
		throwOOB(v, w);
		
		int root = 0;
		for (int i = 0; i < G.V(); i++) {
			if (!G.adj(i).iterator().hasNext()) root = i;
		}
		Stack<Integer> vStack = (Stack<Integer>)new BreadthFirstDirectedPaths(G,v).pathTo(root);
		Stack<Integer> wStack = (Stack<Integer>)new BreadthFirstDirectedPaths(G,w).pathTo(root);
		Stack<Integer> vStack2 = new Stack<Integer>();
		for (int i = 0; i < vStack.size(); i++) {
			vStack2.push(vStack.pop());
		}
		Stack<Integer> wStack2 = new Stack<Integer>();
		for (int i = 0; i < wStack.size(); i++) {
			wStack2.push(wStack.pop());
		}
		
		int ca = -1;
		while (!vStack2.isEmpty()) {
			if ((vStack2.peek().equals(wStack2.peek()))) {
				ca = vStack2.peek();
				vStack2.pop();
				wStack2.pop();
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
		for (int i : v) throwOOB(i);
		for (int i : w) throwOOB(i);
		
		int minDistance = Integer.MAX_VALUE;
		int l;
		for (int vItem : v) {
			for (int wItem : w) {
				l = length(vItem, wItem);
				if ( l < minDistance) minDistance = l;
			}
		}
		return minDistance;
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
		for (int i : v) throwOOB(i);
		for (int i : w) throwOOB(i);
		
		BreadthFirstDirectedPaths vbfs = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths wbfs = new BreadthFirstDirectedPaths(G, w);
		
		ArrayList<Integer> ancestors = new ArrayList<Integer>();
		
		for (int i = 0; i < G.V(); i ++) {
			if (vbfs.hasPathTo(i) && wbfs.hasPathTo(i)) {
				ancestors.add(i);
			}
		}
		
		int minDistance = Integer.MAX_VALUE;
		int minAncestor = -1;
		for (int n : ancestors) {
			int dist = vbfs.distTo(n) + wbfs.distTo(n);
			if (dist < minDistance) {
				minDistance = dist;
				minAncestor = n;
			}
		}
		
		return minAncestor;
	}
	
	/**
	 * @param v
	 * @param w
	 */
	private void throwOOB(int v, int w) {
		if (v < 0 || v > G.V() -1) throw new IndexOutOfBoundsException("Vertex v is not between 0 and G.V() -1");
		if (w < 0 || w > G.V() -1) throw new IndexOutOfBoundsException("Vertex w is not between 0 and G.V() -1");
	}
	private void throwOOB(int v) {
		if (v < 0 || v > G.V() -1) throw new IndexOutOfBoundsException("Vertex v is not between 0 and G.V() -1");
	}
	
	public static void main(String[] args) {
		// Unit testing
	}
	
}
