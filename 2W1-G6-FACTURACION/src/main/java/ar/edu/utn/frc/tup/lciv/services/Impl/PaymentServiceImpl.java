package ar.edu.utn.frc.tup.lciv.services.Impl;

import ar.edu.utn.frc.tup.lciv.entities.PaymentMethodEntity;
import ar.edu.utn.frc.tup.lciv.models.PaymentMethod;
import ar.edu.utn.frc.tup.lciv.repositories.PaymentMethodJpaRepository;
import ar.edu.utn.frc.tup.lciv.services.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentMethodJpaRepository paymentMethodJpaRepository;
    @Autowired
    private ModelMapper modelMapper;
    //Si el cambio en el metodo create produce algun error por favor avisen, yo lo probe en swagger y postman
    //y sigue funcionando no se si puede surgir algun conflicto en el form pero es la unica forma de que pase los test
    @Override
    public PaymentMethod CreatePaymentMethod(String name) {
        /*PaymentMethodEntity newPaymentMethod = new PaymentMethodEntity();
        newPaymentMethod.setName(name);
        paymentMethodJpaRepository.save(newPaymentMethod);
        return modelMapper.map(newPaymentMethod,PaymentMethod.class);*/

        PaymentMethodEntity newPaymentMethod = new PaymentMethodEntity();
        newPaymentMethod.setName(name);
        PaymentMethodEntity savedPaymentMethod = paymentMethodJpaRepository.save(newPaymentMethod);
        return modelMapper.map(savedPaymentMethod, PaymentMethod.class);
    }

    @Override
    public List<PaymentMethod> GetPaymentMethods() {
        List<PaymentMethodEntity> lista = paymentMethodJpaRepository.findAll();
        List<PaymentMethod> rta = new ArrayList<>();
        for (PaymentMethodEntity entity:lista) {
            PaymentMethod paymentMethod = new PaymentMethod(entity.getId(), entity.getName());
            rta.add(paymentMethod);
        }
        return rta;
    }

    @Override
    public PaymentMethod GetById(Long id) {
       Optional<PaymentMethodEntity> entity= paymentMethodJpaRepository.findById(id);
        if(entity.isEmpty()) {
            throw new EntityNotFoundException(String.format("The paymentMethod id %s do not exist", id));
        }
        return modelMapper.map(entity, PaymentMethod.class);
    }

    @Override
    public boolean DeletePaymentMethod(Long id) {
        paymentMethodJpaRepository.deleteById(id);
        return true;
    }

    @Override
    public PaymentMethod UpdatePaymentMethod(PaymentMethod method) {
        PaymentMethodEntity entity = paymentMethodJpaRepository.getReferenceById(method.getId());
        entity.setName(method.getName());
        paymentMethodJpaRepository.save(entity);
        return modelMapper.map(entity,PaymentMethod.class);
    }
}
