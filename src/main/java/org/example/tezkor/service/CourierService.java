package org.example.tezkor.service;

import lombok.RequiredArgsConstructor;
import org.example.tezkor.dto.CreateCourierRequest;
import org.example.tezkor.enums.Courier;
import org.example.tezkor.enums.User;
import org.example.tezkor.repository.CourierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;

    @Transactional
    public Courier createCourier(User courierUser, CreateCourierRequest request) {
        // Kuryer yaratish - hech qaysi do'konga bog'lanmaydi
        Courier courier = new Courier();
        courier.setUser(courierUser);
        courier.setTransportType(request.getTransportType());
        courier.setAvailable(true); // Dastlab ishlaydi, zakaz qabul qila oladi

        return courierRepository.save(courier);
    }

    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }

    public List<Courier> getAvailableCouriers() {
        return courierRepository.findByAvailableTrue();
    }

    @Transactional
    public Courier toggleAvailability(Long courierId) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Kuryer topilmadi: " + courierId));

        courier.setAvailable(!courier.getAvailable());
        return courierRepository.save(courier);
    }

    public Courier getCourierById(Long courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Kuryer topilmadi: " + courierId));
    }
}