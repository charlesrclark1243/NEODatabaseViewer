package NEODatabase;

public class DuplicateNEOException extends Exception {
    public DuplicateNEOException() {
        super("Invalid Input: NEO with specified reference ID already exists in the database.");
    }
}
