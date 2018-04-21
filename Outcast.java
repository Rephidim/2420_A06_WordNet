package main;

public class Outcast {
	private final WordNet WORDNET;
	
	public Outcast(WordNet wordnet) {
		WORDNET = wordnet;
	}
	
	public String outcast(String[] nouns) {
		int d = 0;
		int maxD = 0;
		String oc = "";
		for (String s : nouns) {
			for (String t : nouns) {
				if (!s.equals(t)) d +=  WORDNET.distance(s, t);
			}
			if (d > maxD) {
				maxD = d;
				oc = s;
			}
		}
		return oc;
	}
	
	public static void main(String args[]) {
		// unit testing
		Outcast o = new Outcast(new WordNet("synsets.txt","hypernyms.txt"));
		String[] array = {"horse","zebra","cat","bear","table"};
		System.out.println(o.outcast(array));
	}
}
