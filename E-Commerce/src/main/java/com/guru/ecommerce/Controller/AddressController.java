package com.guru.ecommerce.Controller;


import com.guru.ecommerce.Model.User;
import com.guru.ecommerce.Payload.AddressDTO;
import com.guru.ecommerce.Service.Address.AddressService;
import com.guru.ecommerce.Utils.AuthUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO address) {

        User user=authUtil.loggedInUser();
        AddressDTO savedAddressDTO=addressService.createAddress(address,user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        List<AddressDTO> addresses=addressService.getAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addresses=addressService.getAddressById(addressId);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddressesByUser(){
        User user=authUtil.loggedInUser();
        List<AddressDTO> addresses=addressService.getUserAddresses(user);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId,@RequestBody AddressDTO address){
        AddressDTO addresses=addressService.updateAddress(addressId,address);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
       String status= addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
