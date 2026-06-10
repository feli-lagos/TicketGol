package ticketgol.merch_ordenes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_merchandising") // Ajusta el nombre de la tabla según tu negocio
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    @Max(value = 999, message = "La cantidad máxima permitida es 3 dígitos (999)")
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @NotNull
    @Column(name = "precio_unitario", nullable = false)
    private int precioUnitario;

    @NotNull
    @Column(name = "precio_total", nullable = false)
    private int precioTotal;

    @NotNull
    @Column(name = "fecha_orden", nullable = false)
    private LocalDateTime fechaOrden;

    @Column(name = "status", nullable = false)
    private boolean status;

    @NotNull
    @Column(name = "merchandise_id", nullable = false)
    private Long merchandiseId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @PrePersist
    protected void onCreate() {
        this.fechaOrden = LocalDateTime.now();
        this.status = true; // Por defecto, la orden nace activa
    }
}