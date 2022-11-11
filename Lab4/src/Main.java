import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FiniteAutomata finiteAutomata = new FiniteAutomata("E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab4\\src\\FA.in");

        printMenu();
        Scanner reader = new Scanner(System.in);
        int option = reader.nextInt();

        while (option != 0) {
            switch (option) {
                case 10 -> runScanner();
                case 1 -> System.out.println(finiteAutomata.writeStates());
                case 2 -> System.out.println(finiteAutomata.writeAlphabet());
                case 3 -> System.out.println(finiteAutomata.initialState);
                case 4 -> System.out.println(finiteAutomata.writeTransitions());
                case 5 -> System.out.println(finiteAutomata.writeFinalStates());
                case 6 -> {
                    System.out.println("\nIntroduce the sequence: ");
                    Scanner reader2 = new Scanner(System.in);
                    String sequence = reader2.nextLine();

                    if (finiteAutomata.checkSequence(sequence)) {
                        System.out.println("The sequence is valid!");
                    } else {
                        System.out.println("Invalid sequence!");
                    }
                }
            }
            System.out.println("\n");
            printMenu();
            option = reader.nextInt();
        }
    }

    private static void printMenu() {
        System.out.println("10. Run the scanner;");
        System.out.println("1. Print the set of states;");
        System.out.println("2. Print the alphabet;");
        System.out.println("3. Print the initial state;");
        System.out.println("4. Print all the transitions;");
        System.out.println("5. Print the final states;");
        System.out.println("6. Check if sequence is accepted by the FA.");
        System.out.println("0. Exit.");
        System.out.println("\nChoose an option: ");
    }

    private static void runScanner() throws FileNotFoundException {
        String programFile1 = "E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab4\\src\\Program2.txt";
        String symbolTableFile1 = "E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab4\\src\\ST.out";
        String programInterFormFile1 = "E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab4\\src\\PIF.out";

        TheScanner scanner1 = new TheScanner(programFile1, symbolTableFile1, programInterFormFile1);
        try {
            scanner1.scan();
//            scanner1.printSymbolTable();
//            scanner1.printProgramInternalForm();
            scanner1.writeInFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
