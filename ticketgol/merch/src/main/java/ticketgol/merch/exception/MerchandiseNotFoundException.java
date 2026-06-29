package ticketgol.merch.exception;

public class MerchandiseNotFoundException extends RuntimeException {
    public MerchandiseNotFoundException(String message) {
        super(message);
    }
}