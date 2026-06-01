package com.snapBuy.project.service;

import com.snapBuy.project.exceptions.ResourceNotFoundException;
import com.snapBuy.project.model.Address;
import com.snapBuy.project.model.User;
import com.snapBuy.project.payload.AddressDTO;
import com.snapBuy.project.repositories.AddressRepository;
import com.snapBuy.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of AddressService.
 * Handles business logic for address management including
 * entity mapping, user association, validation handling,
 * and database operations.
 */

@Service
public class AddressServiceImpl implements AddressService{

    // Repository for Address entity database operations
    @Autowired
    private AddressRepository addressRepository;


    // Used for converting between Entity and DTO
    @Autowired
    private ModelMapper modelMapper;

    // Repository for User entity operations
    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {

        // Convert DTO to Entity
        Address address = modelMapper.map(addressDTO, Address.class);

        // Associate address with user
        address.setUser(user);

        // Maintain bidirectional relationship
        List<Address> addressesList = user.getAddresses();
        addressesList.add(address);
        user.setAddresses(addressesList);

        // Save address to a database
        Address savedAddress = addressRepository.save(address);

        // Convert Entity back to DTO
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    /**
     * Fetch all addresses from database and convert to a DTO list.
     */
    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    /**
     * Retrieve address by ID with exception handling if not found.
     */
    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    /**
     * Retrieve all addresses belonging to a specific user.
     */
    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    /**
     * Update existing address details and maintain user relationship consistency.
     */
    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {

        // Fetch existing address
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        // Update fields
        addressFromDatabase.setCity(addressDTO.getCity());
        addressFromDatabase.setPincode(addressDTO.getPincode());
        addressFromDatabase.setState(addressDTO.getState());
        addressFromDatabase.setCountry(addressDTO.getCountry());
        addressFromDatabase.setStreet(addressDTO.getStreet());
        addressFromDatabase.setBuildingName(addressDTO.getBuildingName());

        // Save updated entity
        Address updatedAddress = addressRepository.save(addressFromDatabase);

        // Maintain user-address relationship consistency
        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }


    /**
     * Delete an address and remove its reference from the user entity.
     */
    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = addressFromDatabase.getUser();

        // Remove address from user's address list
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);

        // Delete address from a database
        addressRepository.delete(addressFromDatabase);

        return "Address deleted successfully with addressId: " + addressId;
    }
}