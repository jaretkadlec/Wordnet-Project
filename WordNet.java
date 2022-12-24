import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class WordNet {

    // private variables
    private final Digraph G;
    private final ST<String, Integer> nouns;
    private final ST<Integer, String> synsets;
    private final SAP scan;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        this.nouns = new ST<String, Integer>();
        this.synsets = new ST<Integer, String>();
        // Read in all synsets (and do something with them)
        int N = 0;
        In synsetsIn = new In(synsets);
        while (synsetsIn.hasNextLine()) {
            String line = synsetsIn.readLine();
            String parts[] = line.split(",");
            int synId = Integer.parseInt(parts[0]);
            String synStr = parts[1];
            String[] synset = synStr.split(" ");
            this.synsets.put(synId, synStr);
            for (int i = 0; i < synset.length; i++) {
                if (!nouns.contains(synset[i])) {
                    nouns.put(synset[i], synId);
                    N += 1;
                }
                
            }
        }
        synsetsIn.close();

        // Read in all hypernyms with some similar code
        G = new Digraph(N);
        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String line = hypernymsIn.readLine();
            String parts[] = line.split(",");
            int hypId = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int hyp = Integer.parseInt(parts[i]);
                G.addEdge(hypId, hyp);
            }
        }
        hypernymsIn.close();
        Topological testTopology = new Topological(G);
        if (!testTopology.hasOrder()) {
            throw new IllegalArgumentException();
        }
        scan = new SAP(G);
    }

   // all WordNet nouns
   public Iterable<String> nouns(){
       return nouns; //fixme
   }


   // is the word a WordNet noun?
   public boolean isNoun(String word) {
       if (word == null) {
           throw new IllegalArgumentException();
       }
       return nouns.contains(word); //fixme
   }


   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
   public String sap(String noun1, String noun2) {
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException();
        }
        int noun1Index = nouns.get(noun1);
        int noun2Index = nouns.get(noun2);
        int ancestorIndex = scan.ancestor(noun1Index, noun2Index);
        return synsets.get(ancestorIndex); //fixme
   }

   // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException();
        }
        int noun1Index = nouns.get(noun1);
        int noun2Index = nouns.get(noun2);
        return scan.length(noun1Index, noun2Index);
    }
   

   // do unit testing of this class
    public static void main(String[] args) { //"throw" because the constructor throws.
        // WordNet wnet = new WordNet("synsets.txt", "hypernyms.txt");
        // int dist = wnet.distance("Steinberg", "agaric");
        // StdOut.println("distance: " + dist);
        //how to test
        // In in = new In(args[0]);
        // Digraph G = new Digraph(in);
        // SAP sap = new SAP(G);
        // while (!StdIn.isEmpty()) {
        //     int v = StdIn.readInt();
        //     int w = StdIn.readInt();
        //     int length = sap.length(v, w);
        //     int ancestor = sap.ancestor(v, w);
        //     StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }
    }
}