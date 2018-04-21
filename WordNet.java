package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
	private HashMap<Integer,String[]> synMap;
	private HashMap<String, Set<Integer>> wordToID = new HashMap<String, Set<Integer>>();
	private final Digraph G;
	private final SAP sap;

	/**
	 * Constructor takes the name of two input files.
	 * @param synsets - input file 1
	 * @param hypernyms - input file 2
	 * @throws NullPointerException
	 */
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) throw new NullPointerException("Args cannot be null");
		
		In in = new In(synsets);
		String delim = ",";
		
		initSynMap(in, delim);
		G = initHyps(hypernyms);
		sap = new SAP(G);
		if (!sap.isRootedDAG()) {
			throw new IllegalArgumentException("Must create a rootedDAG");
		}
	}

	/**
	 * @param in
	 * @param delim
	 */
	private void initSynMap(In in, String delim) {
		synMap = new HashMap<Integer, String[]>();
		String tempSplit = " ";
		String[] stringArray;
		int x;
		HashSet<Integer> hs;
		while (in.hasNextLine()) {
			// builds synMap
			String[] holder = in.readLine().split(delim);
			stringArray = holder[1].split(tempSplit);
			x = Integer.parseInt(holder[0]);
			synMap.put(x, stringArray);
			
			// builds wordToID
			for (String n : stringArray) {
				if (wordToID.containsKey(n)) {
					wordToID.get(n).add(x);
				} else {
					hs = new HashSet<Integer>();
					hs.add(x);
					wordToID.put(n, hs);
				}
			}
		}
	}
	
	/**
	 * Adds edges to G in time proportional to # of 
	 * entries in hypernyms.txt
	 * @param hypernyms
	 * @return
	 */
	private Digraph initHyps(String hypernyms) {
        Digraph graph = new Digraph(synMap.size());

        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            Integer synMapId = Integer.valueOf(line[0]);
            for (int i = 1; i < line.length; i++) {
                Integer id = Integer.valueOf(line[i]);
                graph.addEdge(synMapId, id);
            }
        }
        return graph;
    }
	
	/**
	 * returns all WordNet nouns
	 */
	public Iterable<String> nouns() {
		if (!synMap.isEmpty()) {
			return wordToID.keySet();
		} else return null;
	}
	
	/**
	 * is the word a WordNet noun?
	 * @param word
	 * @throws NullPointerException
	 * @returns
	 */
	public boolean isNoun(String word) {
		if (word == null) throw new NullPointerException("Args cannot be null");
		return wordToID.containsKey(word);
	}
	
	/**
	 * distance between nounA and nounB
	 * @param nounA
	 * @param nounB
	 * @throws NullPointerException
	 * @return
	 */
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null) throw new NullPointerException("Args cannot be null");
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new IllegalArgumentException("tried to make sap with not nouns");
		}
		return sap.length(wordToID.get(nounA), wordToID.get(nounB));
	}
	
	/**
	 * a synset (2nd field of synsets.txt) that is the common ancestor of NounA and nounB
	 * in a shortest ancestral path.
	 * @param nounA
	 * @param nounB
	 * @throws NullPointerException
	 * @return
	 */
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null) throw new NullPointerException("Args cannot be null");
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new IllegalArgumentException("tried to make sap with not nouns");
		}
		Set<Integer> wordIDA = wordToID.get(nounA);
		Set<Integer> wordIDB = wordToID.get(nounB);
		int commonA = sap.ancestor(wordIDA, wordIDB);
		StringBuilder sb = new StringBuilder();
		for (String s : synMap.get(commonA)) {
			sb.append(s + " ");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		WordNet w = new WordNet("synsets.txt","hypernyms.txt");
		System.out.println(w.sap("abulia", "absurd"));
	}
	
}
