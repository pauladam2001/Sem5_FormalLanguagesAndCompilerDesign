import java.io.IOException;

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

        String programFile1 = "E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab3\\Lab3-SymbolTable\\src\\Program2.txt";
        String symbolTableFile1 = "E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab3\\Lab3-SymbolTable\\src\\ST.out";
        String programInterFormFile1 = "E:\\AAFacultate\\Anul 3 Semestrul 1\\Limbaje Formale si Tehnici de Compilare\\Lab\\Lab3\\Lab3-SymbolTable\\src\\PIF.out";

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
