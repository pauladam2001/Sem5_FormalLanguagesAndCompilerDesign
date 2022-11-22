import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Grammar grammar = new Grammar("E:\\AAFacultate\\Anul 3 Semestrul 1\\Formal Languages and Compiler Design\\Labs\\Lab5\\src\\g2.txt");

        System.out.println();
        if (grammar.checkContextFreeGrammar()) {
            System.out.println("Context Free Grammar!");
        } else {
            System.out.println("Not a Context Free Grammar!");
        }
        System.out.println();
        System.out.println(grammar.printNonTerminals());
        System.out.println(grammar.printTerminals());
        System.out.println(grammar.printProductions());
    }
}
