package ar.edu.utn.frc.tup.lciv.repositories;

import ar.edu.utn.frc.tup.lciv.entities.InvoiceEntity;
import ar.edu.utn.frc.tup.lciv.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceEntityJpaRepository extends JpaRepository<InvoiceEntity,Long> {

    @Query("SELECT i FROM InvoiceEntity i " +
            "WHERE (COALESCE(:dateFrom, null) IS NULL OR i.date >= :dateFrom) " +
            "AND (COALESCE(:dateTo, null) IS NULL OR i.date <= :dateTo) " +
            "AND (:clientId IS NULL OR i.clientId = :clientId)")
    List<InvoiceEntity> getInvoicesByFilter(@Param("dateFrom") Date dateFrom,
                                            @Param("dateTo") Date dateTo,
                                            @Param("clientId") Long clientId);

}
