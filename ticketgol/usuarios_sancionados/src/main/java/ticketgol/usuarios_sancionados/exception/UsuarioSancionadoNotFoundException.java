package ticketgol.usuarios_sancionados.exception;

public class UsuarioSancionadoNotFoundException extends RuntimeException {
    public UsuarioSancionadoNotFoundException(String message) {
        super(message);
    }
}