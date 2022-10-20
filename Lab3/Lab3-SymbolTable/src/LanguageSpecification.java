import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageSpecification {
    private final ArrayList<String> separators = new ArrayList<>(Arrays.asList("(", ")", "{", "}","[", "]", " ", "\t", "\n"));
    private final ArrayList<String> operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "=", "==", "!=", "<", ">", "<=", ">=", "&&", "||", "++", "--"));
    private final ArrayList<String> partOfOperators = new ArrayList<>(Arrays.asList("=", "!", "<", ">", "&", "|", "+", "-"));
    private final ArrayList<String> reservedWords = new ArrayList<>(Arrays.asList("integer", "char", "string", "FOR", "WHILE", "if", "elseif", "else", "puts", "break", "return", "next", "end"));

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
        final Pattern pattern = Pattern.compile("_[a-z0-9]+_");
        final Matcher matcher = pattern.matcher(token);

        return matcher.matches();
    }

    public boolean isConstant(String token) {
        String number = "^0|[+|-][1-9]([0-9])*|[1-9]([0-9])*$";
        String character = "^\'[a-zA-Z0-9_?!#*./%+=<>;)(}{ ]\'";
        String string = "^\"[a-zA-Z0-9_?!#*./%+=<>;)(}{ ]+\"";

        return token.matches(number) || token.matches(character) || token.matches(string);
    }

    public boolean isPartOfOperator(String token) {
        return partOfOperators.contains(token) || isOperator(String.valueOf(token));
    }
}
