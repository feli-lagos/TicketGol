package ticketgol.compras.Exception;

public class ComprasNotFoundException extends RuntimeException {

    public ComprasNotFoundException(String message) {
        super(message);
    }

    public ComprasNotFoundException(Long id) {
        super("No se encontró una compra con el ID: " + id);
    }
}