import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
//        SymbolTable symbolTable = new SymbolTable(10);
//        System.out.println(symbolTable.add("a"));
//        System.out.println(symbolTable.add("b"));
//        System.out.println(symbolTable.add("ab"));
//        System.out.println("\n");
//        System.out.println(symbolTable.find("a"));
//        System.out.println(symbolTable.find("b"));
//        System.out.println(symbolTable.find("ab"));
//        System.out.println(symbolTable.find("c"));
//        System.out.println(symbolTable.find("abc"));
//        System.out.println(symbolTable);
//        System.out.println("-------------------------------------------");

        TheScanner scanner1 = new TheScanner("E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab3\\Lab3-SymbolTable\\src\\Program1.txt");
        try {
            scanner1.scan();
            scanner1.printSymbolTable();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("-------------------------------------------");
        TheScanner scanner2 = new TheScanner("E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab3\\Lab3-SymbolTable\\src\\Program1Error.txt");
        try {
            scanner2.scan();
            scanner2.printSymbolTable();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
