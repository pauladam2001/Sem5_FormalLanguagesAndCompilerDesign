import java.util.ArrayList;

public class ProgramInternalForm {
    private ArrayList<Pair<String, Pair<Integer, Integer>>> programInternalForm;

    public ProgramInternalForm() {
        programInternalForm = new ArrayList<>();
    }

    public void add(String key, Pair<Integer, Integer> value) {
        Pair<String, Pair<Integer, Integer>> pair = new Pair<>(key, value);
        programInternalForm.add(pair);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Pair<String, Pair<Integer, Integer>> pair : programInternalForm) {
            result.append(pair.getKey()).append(": (").append(pair.getValue().getKey()).append(", ").append(pair.getValue().getValue()).append(")");
            result.append("\n");
        }

        return result.toString();
    }
}
