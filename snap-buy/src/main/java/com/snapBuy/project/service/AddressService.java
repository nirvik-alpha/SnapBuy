package com.snapBuy.project.service;

import com.snapBuy.project.model.User;
import com.snapBuy.project.payload.AddressDTO;

import java.util.List;

/**
 * Service interface for managing user addresses.
 * Provides business operations related to:
 * - Creating addresses
 * - Retrieving addresses
 * - Updating addresses
 * - Deleting addresses
 */

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressesById(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);


}
