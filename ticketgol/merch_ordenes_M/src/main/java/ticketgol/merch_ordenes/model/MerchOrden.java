package ticketgol.merch_ordenes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Merchandise_orders")
public class MerchOrden {
    private Long id;
    private int cantidad;
    private int precioUnitario;
    private int precioTotal;
    private Date fechaOrden;
    private boolean status;
    private Long merchandiseId;
    private Long userId;
}
