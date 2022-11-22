import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grammar {
    private Set<String> N = new HashSet<>();    // non-terminals
    private Set<String> E = new HashSet<>();    // terminals (operators, separators, reserved words, identifiers, constants)
    private HashMap<Set<String>, Set<List<String>>> P = new HashMap<>();    // productions (rules)
    private String S = "";   // start symbol

    public Grammar(String file) throws IOException {
        readFile(file);
    }

    private void readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String nonTerminalsLine = reader.readLine();
        N = new HashSet<>(Arrays.asList(nonTerminalsLine.strip().split(" ")));

        String terminalsLine = reader.readLine();
        E = new HashSet<>(Arrays.asList(terminalsLine.strip().split(" ")));

        S = reader.readLine().strip();

        // all the remaining lines are productions
        String production = reader.readLine();
        while (production != null) {
            String[] tokens = production.split("->");
            String[] leftTokens = tokens[0].split(",");
            String[] rightTokens = tokens[1].split("\\|");

            Set<String> left = new HashSet<>();
            for (String token : leftTokens) {
                left.add(token.strip());
            }
            if (!P.containsKey(left)) {
                P.put(left, new HashSet<>());
            }

            for (String token : rightTokens) {
                ArrayList<String> productionRightElements = new ArrayList<>();
                String[] rightTokenElements = token.strip().split(" ");
                for (String r : rightTokenElements) {
                    productionRightElements.add(r.strip());
                }
                P.get(left).add(productionRightElements);
            }

            production = reader.readLine();
        }
    }

    public boolean checkContextFreeGrammar() {
        boolean checkStartingSymbol = false;
        for (Set<String> left : P.keySet()) {
            if (left.contains(S)) {
                checkStartingSymbol = true;
                break;
            }
        }

        if (!checkStartingSymbol) {
            return false;
        }

        for (Set<String> left : P.keySet()) {
            if (left.size() < 1) {
                return false;
            } else if (!N.contains(left.iterator().next())) {
                return false;
            }

            Set<List<String>> right = P.get(left);
            for(List<String> tokens : right) {
                for (String token : tokens) {
                    if (!(N.contains(token) || E.contains(token) || token.equals("epsilon"))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public String printNonTerminals() {
        StringBuilder stringBuilder = new StringBuilder("N = { ");
        for (String nonTerminal : N) {
            stringBuilder.append(nonTerminal).append(" ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String printTerminals() {
        StringBuilder stringBuilder = new StringBuilder("E = { ");
        for (String terminal : E) {
            stringBuilder.append(terminal).append(" ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String printProductions() {
        StringBuilder stringBuilder = new StringBuilder("P = {\n");
        P.forEach((left, right) -> {
            stringBuilder.append("\t");
            int comma = 0;
            for (String token : left) {
                stringBuilder.append(token);
                comma += 1;
                if (comma < left.size()) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append(" -> ");
            comma = 0;
            for (List<String> tokens : right) {
                for (String token : tokens) {
                    stringBuilder.append(token).append(" ");
                }
                comma += 1;
                if (comma < right.size()) {
                    stringBuilder.append("| ");
                }
            }
            stringBuilder.append("\n");
        });
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String printProductionsForNonTerminal(String nonTerminal) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Set<String> left : P.keySet()) {
            if (left.contains(nonTerminal)) {
                stringBuilder.append(nonTerminal).append(" -> ");
                Set<List<String>> right = P.get(left);
                int comma = 0;
                for (List<String> tokens : right) {
                    for (String token : tokens) {
                        stringBuilder.append(token).append(" ");
                    }
                    comma += 1;
                    if (comma < right.size()) {
                        stringBuilder.append("| ");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
}
