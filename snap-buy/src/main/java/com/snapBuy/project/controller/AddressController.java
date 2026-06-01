package com.snapBuy.project.controller;


import com.snapBuy.project.model.User;
import com.snapBuy.project.payload.AddressDTO;
import com.snapBuy.project.service.AddressService;
import com.snapBuy.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* * REST Controller responsible for Address Management.
*  * Responsibilities:
*  - Create new addresses
*  - Retrieve all addresses
*  - Retrieve a specific address
*  - Retrieve addresses belonging to the logged-in user
*  - Update existing addresses
*  - Delete addresses
 */


@RestController
@RequestMapping("/api")
public class AddressController {

    // Utility class for retrieving authenticated user information

    @Autowired
    AuthUtil authUtil;

    // Service layer responsible for address-related business logic

    @Autowired
    AddressService addressService;

    /**
     * Create a new address for the currently authenticated user.
     */
    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieve all available addresses.
     */
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        List<AddressDTO> addressList = addressService.getAddresses();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    /**
     * Retrieve a specific address by its identifier.
     */
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO = addressService.getAddressesById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    /**
     * Retrieve all addresses associated with the currently logged-in user.
     */
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressList = addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    /**
     * Update an existing address.
     */
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId
            , @RequestBody AddressDTO addressDTO){
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    /**
     * Delete an address by its identifier.
     */
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> updateAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
