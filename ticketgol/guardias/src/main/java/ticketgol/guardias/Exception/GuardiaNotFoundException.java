package ticketgol.guardias.Exception;

public class GuardiaNotFoundException extends RuntimeException {
    public GuardiaNotFoundException(String message) {
        super(message);
    }
}