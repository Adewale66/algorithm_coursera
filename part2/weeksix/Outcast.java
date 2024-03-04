package weeksix;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Outcast {

    private final WordNet wordNet;
    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public  String outcast(String[] nouns) {
        int idx = -1;
        int maxDistance = 0;

        for (int i = 0; i < nouns.length; i++) {
            int distance = 0;
            for (String noun : nouns) {
                distance += wordNet.distance(nouns[i], noun);
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                idx = i;
            }
        }
        return nouns[idx];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
