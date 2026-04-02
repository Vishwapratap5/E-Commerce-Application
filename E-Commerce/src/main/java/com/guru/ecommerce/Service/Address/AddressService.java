package com.guru.ecommerce.Service.Address;

import com.guru.ecommerce.Model.User;
import com.guru.ecommerce.Payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO address, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddress(Long addressId,AddressDTO address);

    String deleteAddress(Long addressId);
}
