package ar.edu.utn.frc.tup.lciv.repositories;

import ar.edu.utn.frc.tup.lciv.entities.InvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailEntityJpaRepository extends JpaRepository<InvoiceDetailEntity, Long> {
}
