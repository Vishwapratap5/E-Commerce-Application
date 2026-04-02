package com.guru.ecommerce.Service.Address.Implementations;

import com.guru.ecommerce.DAO.AddressRepository;
import com.guru.ecommerce.DAO.UserRepository;
import com.guru.ecommerce.Exceptions.ResourceNotFoundException;
import com.guru.ecommerce.Model.Address;
import com.guru.ecommerce.Model.User;
import com.guru.ecommerce.Payload.AddressDTO;
import com.guru.ecommerce.Service.Address.AddressService;
import com.guru.ecommerce.Utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO address, User user) {
        Address newAddress=modelMapper.map(address,Address.class);
        List<Address> addressList=user.getAddresses();
        addressList.add(newAddress);
        user.setAddresses(addressList);
        newAddress.setUser(user);
       Address savedAddress= addressRepository.save(newAddress);
       return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses=addressRepository.findAll();
        return addresses.stream().map(address->modelMapper.map(address,AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address=addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("Address not found"));
        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses=user.getAddresses();
        return addresses.stream().map(address->modelMapper.map(address,AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId,AddressDTO addressDTO) {
        Address addressFromDB=addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("Address not found"));

        addressFromDB.setCity(addressDTO.getCity());
        addressFromDB.setCountry(addressDTO.getCountry());
        addressFromDB.setStreet(addressDTO.getStreet());
        addressFromDB.setBuildingName(addressDTO.getBuildingName());
        addressFromDB.setPinCode(addressDTO.getPinCode());
        addressFromDB.setState(addressDTO.getState());

        Address updatedAddress=addressRepository.save(addressFromDB);
        User user=authUtil.loggedInUser();

        user.getAddresses().removeIf(address->address.getAddressId().equals(updatedAddress.getAddressId()));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);
        return modelMapper.map(updatedAddress,AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDB=addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("Address not found"));

        User user=authUtil.loggedInUser();
        user.getAddresses().removeIf(address->address.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(addressFromDB);
        return "Address with id: " +addressId +" has been deleted";
    }


}
