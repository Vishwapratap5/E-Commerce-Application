package com.guru.ecommerce.DAO;

import com.guru.ecommerce.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}