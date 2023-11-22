package ar.edu.utn.frc.tup.lciv.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Table(name = "invoice_paymentsmethod")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InvoicePaymentMethodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_invoice_payment")
    private Long id;

    @Column
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "id_payment", referencedColumnName = "id_payment")
    private PaymentMethodEntity paymentMethod;

    @ManyToOne
    @JoinColumn(name = "id_invoice", referencedColumnName = "id_invoice")
    private InvoiceEntity invoice;
    @Column
    private String observations;

    @Override
    public String toString() {
        return "InvoicePaymentMethodEntity{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}

