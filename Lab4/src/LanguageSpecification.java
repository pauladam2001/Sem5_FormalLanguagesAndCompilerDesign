import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageSpecification {
    private final ArrayList<String> separators = new ArrayList<>(Arrays.asList("(", ")", "{", "}","[", "]", " ", "\t", "\n"));
    private final ArrayList<String> operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "=", "==", "!=", "<", ">", "<=", ">=", "&&", "||", "++", "--"));
    private final ArrayList<String> partOfOperators = new ArrayList<>(Arrays.asList("=", "!", "<", ">", "&", "|", "+", "-"));
    private final ArrayList<String> reservedWords = new ArrayList<>(Arrays.asList("integer", "char", "string", "FOR", "WHILE", "if", "elseif", "else", "puts", "break", "return", "next", "end"));
    private final Map<String, Integer> codes = new HashMap<>();

    private final FiniteAutomata integerConstantFA = new FiniteAutomata("E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab4\\src\\integerConstantFA.in");
    private final FiniteAutomata identifierFA = new FiniteAutomata("E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab4\\src\\identifierFA.in");

    public LanguageSpecification() throws FileNotFoundException {
        createCodes();
    }

    public void createCodes() {
        int code = 2;

        codes.put("constant", 0);
        codes.put("identifier", 1);
        for(String separator : separators) {
            codes.put(separator, code);
            code += 1;
        }
        for (String operator : operators) {
            codes.put(operator, code);
            code += 1;
        }
        for (String reservedWord : reservedWords) {
            codes.put(reservedWord, code);
            code += 1;
        }
    }

    public int getCode(String token) {
        return codes.get(token);
    }

    public boolean isSeparator(String token) {
        return separators.contains(token);
    }

    public boolean isOperator(String token) {
        return operators.contains(token);
    }

    public boolean isReservedWord(String token) {
        return reservedWords.contains(token);
    }

    public boolean isIdentifier(String token) {
//        final Pattern pattern = Pattern.compile("_[a-z0-9]+_");
//        final Matcher matcher = pattern.matcher(token);
//
//        return matcher.matches();

        return identifierFA.checkSequence(token);
    }

    public boolean isConstant(String token) {
//        String number = "^0|[+|-][1-9]([0-9])*|[1-9]([0-9])*$";
        String character = "^\'[a-zA-Z0-9_?!#*./%+=<>;)(}{ ]\'";
        String string = "^\"[a-zA-Z0-9_?!#*./%+=<>;)(}{ ]+\"";
//
//        return token.matches(number) || token.matches(character) || token.matches(string);

        return integerConstantFA.checkSequence(token) || token.matches(character) || token.matches(string);
    }

    public boolean isPartOfOperator(String token) {
        return partOfOperators.contains(token) || isOperator(String.valueOf(token));
    }
}
