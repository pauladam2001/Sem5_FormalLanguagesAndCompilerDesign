import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TheScanner {
    private final LanguageSpecification languageSpecification = new LanguageSpecification();
    private SymbolTable symbolTable = new SymbolTable(10);

    private ProgramInternalForm programInternalForm = new ProgramInternalForm();
    private final String programFile;
    private final String symbolTableFile;
    private final String programInternalFormFile;

    private boolean isCorrect = true;

    public TheScanner(String programFile, String symbolTableFile, String programInternalFormFile) throws FileNotFoundException {
        this.programFile = programFile;
        this.symbolTableFile = symbolTableFile;
        this.programInternalFormFile = programInternalFormFile;
    }

    public void scan() throws FileNotFoundException {
        ArrayList<Pair<String, Integer>> pairsOfTokens = new ArrayList<>();
        int lineNumber = 0;
        File file = new File(programFile);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineNumber += 1;
            ArrayList<String> tokens = tokenize(line);
            for (String token : tokens) {
                pairsOfTokens.add(new Pair<>(token, lineNumber));
            }
        }

        scanner.close();

        buildSymbolTableAndProgramInternalForm(pairsOfTokens);
    }

    public ArrayList<String> tokenize(String line) {
        ArrayList<String> tokens = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            if (languageSpecification.isSeparator(String.valueOf(line.charAt(i))) && !(String.valueOf(line.charAt(i))).equals(" ")) {
                tokens.add(String.valueOf(line.charAt(i)));
            } else if (line.charAt(i) == '+') {
                String token = identifyPlus(line, i, tokens);
                tokens.add(token);
                i += token.length() - 1;
            } else if (line.charAt(i) == '-') {
                String token = identifyMinus(line, i, tokens);
                tokens.add(token);
                i += token.length() - 1;
            } else if (line.charAt(i) == '\'') {
                String character = identifyCharConstant(line, i);
                tokens.add(character);
                i += character.length() - 1;
            } else if (line.charAt(i) == '\"') {
                String string = identifyStringConstant(line, i);
                tokens.add(string);
                i += string.length() - 1;
            } else if (languageSpecification.isPartOfOperator(String.valueOf(line.charAt(i)))) {
                String operator = identifyOperator(line, i);
                tokens.add(operator);
                i += operator.length() - 1;
            } else if (line.charAt(i) != ' ') {
                String token = identifyToken(line, i);
                tokens.add(token);
                i += token.length() - 1;
            }
        }

        return tokens;
    }

    public String identifyPlus(String line, int position, ArrayList<String> tokens) {
        // if plus is preceded by an identifier or a constant, then plus is an operator
        if (languageSpecification.isIdentifier(tokens.get(tokens.size() - 1)) || languageSpecification.isConstant(tokens.get(tokens.size() - 1))) {
            return "+";
        }

        // if plus is preceded by an operator or a separator, then assign a positive number or condition with positive number, then plus belongs to a numeric constant
        StringBuilder token = new StringBuilder();
        token.append("+");
        for (int i = position + 1; i < line.length() && (Character.isDigit(line.charAt(i))); i++) {
            token.append(line.charAt(i));
        }
        return token.toString();
    }

    public String identifyMinus(String line, int position, ArrayList<String> tokens) {
        // if minus is preceded by an identifier or a constant, then minus is an operator
        if (languageSpecification.isIdentifier(tokens.get(tokens.size() - 1)) || languageSpecification.isConstant(tokens.get(tokens.size() - 1))) {
            return "-";
        }

        // if minus is preceded by an operator or a separator, then assign a negative number or condition with negative number, then minus belongs to a numeric constant
        StringBuilder token = new StringBuilder();
        token.append("-");
        for (int i = position + 1; i < line.length() && (Character.isDigit(line.charAt(i))); i++) {
            token.append(line.charAt(i));
        }
        return token.toString();
    }

    public String identifyCharConstant(String line, int position) {
        StringBuilder character = new StringBuilder();

        for (int i = position; i < line.length(); i++) {
            if ((languageSpecification.isSeparator(String.valueOf(line.charAt(i))) || languageSpecification.isOperator(String.valueOf(line.charAt(i)))) && ((i == line.length() - 2 && line.charAt(i + 1) != '\'') || (i == line.length() - 1))) {
                break;
            }
            character.append(line.charAt(i));
            if (line.charAt(i) == '\'' && i != position) {
                break;
            }
        }

        return character.toString();
    }

    public String identifyStringConstant(String line, int position) {
        StringBuilder string = new StringBuilder();

        for (int i = position; i < line.length(); i++) {
            if ((languageSpecification.isSeparator(String.valueOf(line.charAt(i))) || languageSpecification.isOperator(String.valueOf(line.charAt(i)))) && ((i == line.length() - 2 && line.charAt(i + 1) != '\"') || (i == line.length() - 1))) {
                break;
            }
            string.append(line.charAt(i));
            if (line.charAt(i) == '\"' && i != position) {
                break;
            }
        }

        return string.toString();
    }

    public String identifyOperator(String line, int position) {
        StringBuilder operator = new StringBuilder();
        operator.append(line.charAt(position));
        operator.append(line.charAt(position + 1));

        if (languageSpecification.isOperator(operator.toString())) {
            return operator.toString();
        }

        return String.valueOf(line.charAt(position));
    }

    public String identifyToken(String line, int position) {
        StringBuilder token = new StringBuilder();

        for (int i = position; i < line.length(); i++) {
            if (languageSpecification.isSeparator(String.valueOf(line.charAt(i))) || languageSpecification.isPartOfOperator(String.valueOf(line.charAt(i))) || line.charAt(i) == ' ') {
                break;
            }
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    public void buildSymbolTableAndProgramInternalForm(ArrayList<Pair<String, Integer>> pairsOfTokens) {
        ArrayList<String> invalidTokens = new ArrayList<>();

        for (Pair<String, Integer> pair : pairsOfTokens) {
            String token = pair.getKey();
            if (languageSpecification.isOperator(token) || languageSpecification.isReservedWord(token) || languageSpecification.isSeparator(token)) {
                int code = languageSpecification.getCode(token);
                programInternalForm.add(token, new Pair<>(code, -1));
            } else if (languageSpecification.isConstant(token)) {
                symbolTable.add(token);         // only unique elements
                int positionInSymbolTable = symbolTable.find(token);
                programInternalForm.add(token, new Pair<>(0, positionInSymbolTable));
            } else if (languageSpecification.isIdentifier(token)) {
                symbolTable.add(token);         // only unique elements
                int positionInSymbolTable = symbolTable.find(token);
                programInternalForm.add(token, new Pair<>(1, positionInSymbolTable));
            } else if (!invalidTokens.contains(token)) {
                invalidTokens.add(token);
                isCorrect = false;
                System.out.println("Error at line " + pair.getValue() + "! Invalid token: " + token + ".");
            }
        }

        if (isCorrect) {
            System.out.println("Program is correct!\n");
        }
    }

    public void printSymbolTable() {
        if (isCorrect) {
            System.out.println(symbolTable);
        } else {
            System.out.println("Program is not correct!\n");
        }
    }

    public void printProgramInternalForm() {
        if (isCorrect) {
            System.out.println(programInternalForm);
        }
    }

    public void writeInFiles() throws IOException {
        if (isCorrect) {
            File STFile = new File(symbolTableFile);
            FileWriter STFileWriter = new FileWriter(STFile, false);
            BufferedWriter STWriter = new BufferedWriter(STFileWriter);
            STWriter.write(symbolTable.toString());
            STWriter.close();

            File PIFFile = new File(programInternalFormFile);
            FileWriter PIFFileWriter = new FileWriter(PIFFile, false);
            BufferedWriter PIFWriter = new BufferedWriter(PIFFileWriter);
            PIFWriter.write(programInternalForm.toString());
            PIFWriter.close();
        } else {
            PrintWriter STWriter = new PrintWriter(symbolTableFile);
            STWriter.print("");
            STWriter.close();

            PrintWriter PIFWriter = new PrintWriter(programInternalFormFile);
            PIFWriter.print("");
            PIFWriter.close();
        }
    }
}
