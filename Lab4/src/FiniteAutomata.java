import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteAutomata {
    public Set<String> alphabet;
    public Set<String> states;
    public Set<String> finalStates;
    public Map<Pair<String, String>, Set<String>> transitions;
    public String initialState;

    public FiniteAutomata(String programFile) throws FileNotFoundException {
        this.alphabet = new HashSet<>();
        this.states = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.transitions = new HashMap<>();

        readFA(programFile);
    }

    private void readFA(String programFile) throws FileNotFoundException {
        // states
        // alphabet
        // initialState
        // finalStates
        // transitions

        File file = new File(programFile);
        Scanner reader = new Scanner(file);

        String statesLine = reader.nextLine();
        states = new HashSet<>(Arrays.asList(statesLine.split(" ")));
        String alphabetLine = reader.nextLine();
        alphabet = new HashSet<>(Arrays.asList(alphabetLine.split(" ")));
        initialState = reader.nextLine();
        String finalStatesLine = reader.nextLine();
        finalStates = new HashSet<>(Arrays.asList(finalStatesLine.split(" ")));

        while (reader.hasNextLine()) {
            String transitionLine = reader.nextLine();
            String[] transitionElements = transitionLine.split(" ");

            if (states.contains(transitionElements[0]) && states.contains(transitionElements[2]) && alphabet.contains(transitionElements[1])) {
                Pair<String, String> transitionState = new Pair<>(transitionElements[0], transitionElements[1]);
                boolean match = true;
                Pair<String, String> thatPairKey = null;
                for (Pair<String, String> pairKey: transitions.keySet()) {
                    if (pairKey.key.equals(transitionState.key) && pairKey.value.equals(transitionState.value)) {
                        match = false;
                        thatPairKey = pairKey;
                        break;
                    }
                }
                if (match) {
                    Set<String> transitionsStatesSet = new HashSet<>();
                    transitionsStatesSet.add(transitionElements[2]);
                    transitions.put(transitionState, transitionsStatesSet);
                } else {
                    transitions.get(thatPairKey).add(transitionElements[2]);
                }
            }
        }
    }

    public boolean checkSequence(String sequence) {
        if (sequence.length() == 0) {
            return finalStates.contains(initialState);
        }

        String state = initialState;
        for (int i = 0; i < sequence.length(); i++) {
            Pair<String, String> key = new Pair<>(state, String.valueOf(sequence.charAt(i)));
            boolean match = false;
            for (Pair<String, String> pairKey: transitions.keySet()) {
                if (pairKey.key.equals(key.key) && pairKey.value.equals(key.value)) {
                    match = true;
                    state = transitions.get(pairKey).iterator().next();
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return finalStates.contains(state);
    }

    public String writeAlphabet() {
        StringBuilder builder = new StringBuilder();
        builder.append("Alphabet: ");
        for (String a : alphabet) {
            builder.append(a).append(" ");
        }

        return builder.toString();
    }

    public String writeStates() {
        StringBuilder builder = new StringBuilder();
        builder.append("States: ");
        for (String s : states) {
            builder.append(s).append(" ");
        }

        return builder.toString();
    }

    public String writeFinalStates() {
        StringBuilder builder = new StringBuilder();
        builder.append("Final States: ");
        for (String fs : finalStates) {
            builder.append(fs).append(" ");
        }

        return builder.toString();
    }

    public String writeTransitions() {
        StringBuilder builder = new StringBuilder();
        builder.append("Transitions: \n");
        transitions.forEach((K, V) -> builder.append("<").append(K.key).append(",").append(K.value).append("> -> ").append(V).append("\n"));

        return builder.toString();
    }

    @Override
    public String toString() {
        return "Finite Automata: \n" +
                "\t Alphabet: " + writeAlphabet() + "\n" +
                "\t Initial State: '" + initialState + '\'' + "\n" +
                "\t States: " + writeStates() + "\n" +
                "\t Final States: " + writeFinalStates() + "\n" +
                "\t Transitions: " + writeTransitions();
    }
}
