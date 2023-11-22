package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.models.PaymentMethod;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface PaymentService {
    PaymentMethod CreatePaymentMethod(String name);
    List<PaymentMethod> GetPaymentMethods();
    PaymentMethod GetById(Long id);
    boolean DeletePaymentMethod(Long id);

    PaymentMethod UpdatePaymentMethod(PaymentMethod method);

}
