package ar.edu.utn.frc.tup.lciv.repositories;

import ar.edu.utn.frc.tup.lciv.entities.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethodEntity, Long> {
}
