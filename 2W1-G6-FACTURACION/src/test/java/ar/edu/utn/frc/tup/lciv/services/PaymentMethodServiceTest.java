package ar.edu.utn.frc.tup.lciv.services;
import ar.edu.utn.frc.tup.lciv.entities.PaymentMethodEntity;
import ar.edu.utn.frc.tup.lciv.models.PaymentMethod;
import ar.edu.utn.frc.tup.lciv.repositories.PaymentMethodJpaRepository;
import ar.edu.utn.frc.tup.lciv.services.Impl.PaymentServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PaymentMethodServiceTest {

    @InjectMocks
    PaymentServiceImpl paymentMethodService;
    @Spy
    @Qualifier("modelMapper")
    ModelMapper modelMapper;
    @Mock
    PaymentMethodJpaRepository paymentMethodJpaRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    //Funciona
    @Test
    public void testCreatePaymentMethod() {
        
        String paymentMethodName = "Credit Card";

        PaymentMethodEntity newPaymentMethodEntity = new PaymentMethodEntity();
        newPaymentMethodEntity.setName(paymentMethodName);

        PaymentMethodEntity savedPaymentMethodEntity = new PaymentMethodEntity();
        savedPaymentMethodEntity.setId(1L);
        savedPaymentMethodEntity.setName(paymentMethodName);

        when(paymentMethodJpaRepository.save(any(PaymentMethodEntity.class))).thenReturn(savedPaymentMethodEntity);

        PaymentMethod result = paymentMethodService.CreatePaymentMethod(paymentMethodName);

        assertNotNull(result);
        assertEquals(savedPaymentMethodEntity.getId(), result.getId());
        assertEquals(savedPaymentMethodEntity.getName(), result.getName());
    }

    //funciona
    @Test
    public void testGetPaymentMethods() {
        // Arrange
        PaymentMethodEntity paymentMethodEntity1 = new PaymentMethodEntity(1L, "Efectivo", null);
        PaymentMethodEntity paymentMethodEntity2 = new PaymentMethodEntity(2L, "Tarjeta", null);
        List<PaymentMethodEntity> paymentMethodEntities = Arrays.asList(paymentMethodEntity1, paymentMethodEntity2);

        when(paymentMethodJpaRepository.findAll()).thenReturn(paymentMethodEntities);

        // Act
        List<PaymentMethod> result = paymentMethodService.GetPaymentMethods();

        // Assert
        assertEquals(paymentMethodEntities.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(paymentMethodEntities.get(i).getId(), result.get(i).getId());
            assertEquals(paymentMethodEntities.get(i).getName(), result.get(i).getName());
        }
    }
    //Funciona
    @Test
    public void testGetById() {
        // Arrange
        Long id = 1L;

        PaymentMethodEntity mockPaymentMethodEntity = new PaymentMethodEntity();
        mockPaymentMethodEntity.setId(id);
        mockPaymentMethodEntity.setName("Efectivo");
        PaymentMethod mockPaymentMethod = new PaymentMethod(id, "Efectivo");

        when(paymentMethodJpaRepository.findById(id)).thenReturn(Optional.of(mockPaymentMethodEntity));
        when(modelMapper.map(mockPaymentMethodEntity, PaymentMethod.class)).thenReturn(mockPaymentMethod);
        // Act
        PaymentMethod result = paymentMethodService.GetById(id);

        // Assert

        Assertions.assertEquals(mockPaymentMethod, result);
    }

    //funciona
    @Test
    public void testGetById_NotFound() {
        // Arrange
        Long id = 1L;

        when(paymentMethodJpaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> paymentMethodService.GetById(id));
    }
    //Revisar posible falso positivo
    @Test
    public void testUpdatePaymentMethod() {
        // Arrange
        String name = "Free";
        PaymentMethod paymentMethod = new PaymentMethod(1L, "Tarjeta");
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodEntity();
        paymentMethodEntity.setId(paymentMethod.getId());
        paymentMethodEntity.setName(paymentMethod.getName());

        when(paymentMethodJpaRepository.getReferenceById(any(Long.class))).thenReturn(paymentMethodEntity);
        when(paymentMethodJpaRepository.save(any(PaymentMethodEntity.class))).thenReturn(paymentMethodEntity);

        // Act
        PaymentMethod result = paymentMethodService.UpdatePaymentMethod(paymentMethod);

        // Assert
        assertEquals(paymentMethod.getName(), result.getName());
    }


}
