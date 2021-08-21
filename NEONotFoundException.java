package NEODatabase;

public class NEONotFoundException extends Exception {
    public NEONotFoundException() {
        super("Invalid Input: Specified NEO not found in the database.");
    }
}
