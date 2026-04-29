package com.bookstore.customer.controller;

import com.bookstore.customer.entity.*;
import com.bookstore.customer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerProfileRepository profileRepo;
    private final AddressRepository addressRepo;

    @GetMapping("/details")
    public ResponseEntity<CustomerProfile> getDetails(@RequestHeader("userId") Long userId) {
        return profileRepo.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/details")
    public ResponseEntity<CustomerProfile> createDetails(
            @RequestHeader("userId") Long userId,
            @RequestBody CustomerProfile profile) {
        profile.setUserId(userId);
        return ResponseEntity.ok(profileRepo.save(profile));
    }

    @PutMapping("/details")
    public ResponseEntity<CustomerProfile> updateDetails(
            @RequestHeader("userId") Long userId,
            @RequestBody CustomerProfile updated) {
        return profileRepo.findByUserId(userId).map(p -> {
            p.setFullName(updated.getFullName());
            p.setPhone(updated.getPhone());
            return ResponseEntity.ok(profileRepo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/addresses")
    public ResponseEntity<Address> addAddress(@RequestHeader("userId") Long userId,
                                              @RequestBody Address address) {
        address.setUserId(userId);
        return ResponseEntity.ok(addressRepo.save(address));
    }

    @GetMapping("/addresses")
    public List<Address> getAddresses(@RequestHeader("userId") Long userId) {
        return addressRepo.findByUserId(userId);
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}