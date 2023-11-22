package ar.edu.utn.frc.tup.lciv.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "discount")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descuento")
    private Long id;

    @Column
    private BigDecimal amount;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_invoice", referencedColumnName = "id_invoice")
    private InvoiceEntity invoice;

}
