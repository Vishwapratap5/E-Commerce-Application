package com.guru.ecommerce.Service;

import com.guru.ecommerce.DAO.RoleRepository;
import com.guru.ecommerce.DAO.UserRepository;
import com.guru.ecommerce.Enums.AppRole;
import com.guru.ecommerce.Exceptions.UserAlreadyExistsException;
import com.guru.ecommerce.Model.Role;
import com.guru.ecommerce.Model.User;
import com.guru.ecommerce.Payload.UserRegisterRequestDTO;
import com.guru.ecommerce.Payload.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserAuthServiceImpl implements  UserAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public String getCurrentUserName() {
        String principal= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(principal).orElseThrow(()->new EntityNotFoundException("please Login..!"));
        return user.getUsername();
    }

    @Override
    @Transactional
    public UserResponseDTO localCustomerRegistration(UserRegisterRequestDTO user) {

        if (userRepository.existsByUsername(user.getUserName())) {
            throw new UserAlreadyExistsException("Customer already exists");
        }

        if (userRepository.existsByUsername(user.getUserName())) {
            throw new UserAlreadyExistsException("Customer with this username already exists");
        }


        User newUser= modelMapper.map(user, User.class);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<String> strRoles=user.getRoles();
        Set<Role> roles=new HashSet<>();

        if(strRoles==null){
            Role userRole= roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(()-> new RuntimeException("Error:Role is null"));
            roles.add(userRole);
        }else{
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Role adminRole=roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(()-> new RuntimeException("Error:Role is null"));
                        roles.add(adminRole);
                        break;

                        case "seller":
                            Role sellerRole=roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(()-> new RuntimeException("Error:Role is null"));
                            roles.add(sellerRole);
                            break;

                            default:
                                Role userRole= roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(()-> new RuntimeException("Error:Role is null"));
                                roles.add(userRole);


                }
            });
        }
        newUser.setRoles(roles);
        User savedUser=userRepository.save(newUser);
        return modelMapper.map(savedUser,UserResponseDTO.class);

    }

    @Override
    public UserResponseDTO getCurrentUser() {
        String principal= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(principal).orElseThrow(()->new EntityNotFoundException("please Login..!"));
        ModelMapper modelMapper=new ModelMapper();
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
