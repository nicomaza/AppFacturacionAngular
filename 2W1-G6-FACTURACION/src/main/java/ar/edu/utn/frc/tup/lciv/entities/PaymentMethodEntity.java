package ar.edu.utn.frc.tup.lciv.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_method")
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private Long id;

    @Column
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "paymentMethod")
    private List<InvoicePaymentMethodEntity> invoicePaymentMethodEntities;

    @Override
    public String toString() {
        return "PaymentMethodEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
