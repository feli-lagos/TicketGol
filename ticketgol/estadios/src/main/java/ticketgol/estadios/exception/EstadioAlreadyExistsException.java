package ticketgol.estadios.exception;

public class EstadioAlreadyExistsException extends RuntimeException {
    public EstadioAlreadyExistsException(String message) {
        super(message);
    }
}