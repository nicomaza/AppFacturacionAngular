package ar.edu.utn.frc.tup.lciv.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Table(name = "invoice_detail")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InvoiceDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_detail")
    private Long id;

    @Column
    private String productId;
    @Column
    private String product;
    @Column Integer count;
    @Column String measurementUnit;
    @Column BigDecimal price;


    @ManyToOne
    @JoinColumn(name = "id_invoice", referencedColumnName = "id_invoice")
    private InvoiceEntity invoice;


}

