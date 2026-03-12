import java.util.*;

public class Autocomplete {

    // query -> frequency
    private HashMap<String, Integer> frequencyMap;

    // Trie node class
    class TrieNode {
        HashMap<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
    }

    private TrieNode root;

    public Autocomplete() {
        frequencyMap = new HashMap<>();
        root = new TrieNode();
    }

    // Insert query into Trie
    private void insert(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEnd = true;
    }

    // Update frequency when searched
    public void updateFrequency(String query) {

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);

        insert(query);
    }

    // Collect queries from trie
    private void collectQueries(TrieNode node, String prefix, List<String> results) {

        if (node.isEnd) {
            results.add(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {

            collectQueries(entry.getValue(), prefix + entry.getKey(), results);
        }
    }

    // Search suggestions for prefix
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }

            node = node.children.get(c);
        }

        List<String> queries = new ArrayList<>();
        collectQueries(node, prefix, queries);

        // Sort by frequency
        queries.sort((a, b) -> frequencyMap.get(b) - frequencyMap.get(a));

        return queries.subList(0, Math.min(10, queries.size()));
    }

    // Main method for testing
    public static void main(String[] args) {

        Autocomplete system = new Autocomplete();

        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java tutorial");

        List<String> suggestions = system.search("jav");

        System.out.println("Suggestions for 'jav':");

        for (String s : suggestions) {
            System.out.println(s + " (" + system.frequencyMap.get(s) + " searches)");
        }
    }
}