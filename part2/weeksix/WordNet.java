package weeksix;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycleX;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;

public class WordNet {
    private int count = 0;
    private final Digraph digraph;
    private final Set<String> nouns = new TreeSet<>();

    private final List<List<String>> synsets = new ArrayList<>();
    private final SAP sap;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();
        In synInput = new In(synsets);
        In hypInput = new In(hypernyms);
        storeNouns(synInput);
        digraph = new Digraph(count);
        addEdges(hypInput);
        validateGraph(digraph);
        sap = new SAP(digraph);
    }

    private void validateGraph(Digraph graph) {
        DirectedCycleX cycleX = new DirectedCycleX(graph);
        if (cycleX.hasCycle())
            throw new IllegalArgumentException();
        int outdegree = 0;
        for (int i = 0; i < count; i++) {
            if (graph.outdegree(i) == 0)
                outdegree++;
        }
        if (outdegree != 1)
            throw new IllegalArgumentException();
    }

    private void storeNouns(In input) {
        while (!input.isEmpty()) {
            count++;
            List<String> sysnet = Arrays.asList(input.readLine().split(",")[1].split(" "));
            synsets.add(sysnet);
            nouns.addAll(sysnet);
        }
    }

    private void addEdges(In input) {
        while (!input.isEmpty()) {
            String[] ids = input.readLine().split(",");
            int vertex = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++)
                digraph.addEdge(vertex, Integer.parseInt(ids[i]));
        }

    }

    private List<Integer> getVertex(String noun) {
        List<Integer> vertex = new ArrayList<>();
        List<String> synset;
        for (int i = 0; i < synsets.size(); i++) {
            synset = synsets.get(i);
            for (String s : synset) {
              if (s.equals(noun)) {
                  vertex.add(i);
              }
            }
        }
        return vertex;
    }

    private void validate(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validate(nounA, nounB);
        List<Integer> nounAVertex = getVertex(nounA);
        List<Integer> nounBVertex = getVertex(nounB);
        return sap.length(nounAVertex, nounBVertex);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validate(nounA, nounB);
        List<Integer> nounAVertex = getVertex(nounA);
        List<Integer> nounBVertex = getVertex(nounB);
        int sca = sap.ancestor(nounAVertex, nounBVertex);
        StringBuilder s = new StringBuilder();
        for (String i : synsets.get(sca)) {
            s.append(i);
            s.append(" ");
        }
        return s.toString();
    }
}
