import java.util.ArrayList;

public class SymbolTable {
    private ArrayList<ArrayList<Pair<String, Integer>>> elements2;
    private final int size;
    private int counter;

    public SymbolTable(int size) {
        this.size = size;
        counter = 0;
        elements2 = new ArrayList<>();
        for (int i = 0; i < size; i++)
            elements2.add(new ArrayList<>());
    }

    private int getHash(String key) {
        int hash = 7;
        for (int i = 0; i < key.length(); i++) {
            hash = hash * 3 + key.charAt(i);
        }

        return hash % size;
    }

    public boolean add(String key) {
        int hash = getHash(key);

        if (find(key) == -1) {
            elements2.get(hash).add(new Pair<>(key, counter));
            counter += 1;
            return true;
        }
        return false;
    }

    public int find(String key) {
        int hash = getHash(key);

        ArrayList<Pair<String, Integer>> pairs = elements2.get(hash);
        for (Pair<String, Integer> pair : pairs) {
            if (pair.getKey().equals(key)) {
                return pair.getValue();
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < size; i++) {
            result.append(i).append(": [");
            for(Pair<String, Integer> element: elements2.get(i)){
                result.append(element);
                result.append(" ");
            }
            result.append("]\n");
        }

        return result.toString();
    }
}
