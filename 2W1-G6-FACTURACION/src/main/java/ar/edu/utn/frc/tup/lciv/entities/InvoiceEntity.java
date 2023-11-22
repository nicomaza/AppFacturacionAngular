package ar.edu.utn.frc.tup.lciv.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "invoice")
@Name("TblMaster")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InvoiceEntity {

    @Id
    @Column(name = "id_invoice")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long clientId;
    @Column
    private Date date;
    @Column
    private String cuit;
    @Column
    private String razonSocial;
    @Column
    private Long nroPedido;
    @Column
    private String status;
    @Column
    private String type;
    @Column
    private BigDecimal totalAmount;

    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoicePaymentMethodEntity> paymentMethodEntities;

    @Transient
    private List<InvoicePaymentMethodEntity> paymentMethodsList;


    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @OneToMany(mappedBy = "invoice")
    private List<InvoiceDetailEntity> details;

    @Transient
    private List<InvoiceDetailEntity> detailsList;

    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @OneToMany(mappedBy = "invoice")
    private List<DiscountEntity> discoutns;

    @Transient
    private List<DiscountEntity> discountsList;

    @Column
    private BigDecimal iva;
    @Column
    private BigDecimal ivaAmount;
    @Column
    private BigDecimal totalAmountWithoutIva;


    @Override
    public String toString() {
        return "InvoiceEntity{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", date=" + date +
                ", cuit='" + cuit + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", nroPedido=" + nroPedido +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }

}
