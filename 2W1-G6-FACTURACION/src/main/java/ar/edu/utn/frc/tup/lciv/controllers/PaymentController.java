package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.models.PaymentMethod;
import ar.edu.utn.frc.tup.lciv.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("api/v1/paymentMethods/{id}")
    public ResponseEntity<PaymentMethod> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.GetById(id));
    }
    @GetMapping("api/v1/paymentMethods")
    public ResponseEntity<List<PaymentMethod>> getAll() {
        return ResponseEntity.ok(paymentService.GetPaymentMethods());
    }
    @PostMapping("api/v1/paymentMethods/{name}")
    public ResponseEntity<PaymentMethod> Create(@PathVariable String name){
        return ResponseEntity.ok(paymentService.CreatePaymentMethod(name));
    }

    @DeleteMapping("api/v1/paymentMethod/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.DeletePaymentMethod(id));
    }
    @PutMapping("api/v1/updatePaymentMethod")
    public ResponseEntity<PaymentMethod> Update(@RequestBody PaymentMethod method){
        return ResponseEntity.ok(paymentService.UpdatePaymentMethod(method));
    }
}
