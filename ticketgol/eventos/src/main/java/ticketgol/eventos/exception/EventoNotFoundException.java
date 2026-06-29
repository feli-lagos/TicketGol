package ticketgol.eventos.exception;

public class EventoNotFoundException extends RuntimeException {
    public EventoNotFoundException(String message) {
        super(message);
    }
}