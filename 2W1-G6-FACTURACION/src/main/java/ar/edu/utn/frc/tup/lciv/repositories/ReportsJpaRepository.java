package ar.edu.utn.frc.tup.lciv.repositories;

import ar.edu.utn.frc.tup.lciv.entities.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportsJpaRepository extends JpaRepository<InvoiceEntity,Long> {

    @Query("SELECT i.clientId, SUM(i.totalAmount) AS montoTotal " +
            "FROM InvoiceEntity i " +
            "JOIN InvoiceDetailEntity ide ON i.id = ide.invoice.id " +
            "JOIN InvoicePaymentMethodEntity ipm ON i.id = ipm.invoice.id " +
            "WHERE (COALESCE(:dateFrom,null) IS NULL OR i.date >= :dateFrom) " +
            "AND (COALESCE(:dateTo,null) IS NULL OR i.date <= :dateTo) " +
            "GROUP BY i.clientId")
    List<Object[]> getInvoicesByDateRange(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    @Query("SELECT ipm.paymentMethod.name, SUM(ipm.amount) " +
            "FROM InvoicePaymentMethodEntity ipm " +
            "WHERE (COALESCE(:dateFrom,null) IS NULL OR ipm.invoice.date >= :dateFrom) " +
            "AND (COALESCE(:dateTo,null) IS NULL OR ipm.invoice.date <= :dateTo) " +
            "GROUP BY ipm.paymentMethod.name")
    List<Object[]> getTotalAmountByPaymentMethod(
            @Param("dateFrom") Date dateFrom,
            @Param("dateTo") Date dateTo
    );

    @Query(value = "SELECT MONTH(i.date) AS month, SUM(i.totalAmount) AS total " +
            "FROM InvoiceEntity i " +
            "WHERE (COALESCE(:dateFrom, null) IS NULL OR i.date >= :dateFrom) " +
            "AND (COALESCE(:dateTo, null) IS NULL OR i.date <= :dateTo) " +
            "GROUP BY MONTH(i.date)")
    List<Object[]> getMonthlyTotalAmount(@Param("dateFrom") Date dateFrom,
                                         @Param("dateTo") Date dateTo);

}
