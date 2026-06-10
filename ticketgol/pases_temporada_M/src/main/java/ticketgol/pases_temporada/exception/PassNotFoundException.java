package ticketgol.pases_temporada.exception;

public class PassNotFoundException extends RuntimeException {
    public PassNotFoundException(String message) {
        super(message);
    }
}
