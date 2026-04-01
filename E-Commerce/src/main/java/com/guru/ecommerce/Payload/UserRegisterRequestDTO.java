package com.guru.ecommerce.Payload;

import com.guru.ecommerce.Model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterRequestDTO {
    @NotBlank(message = "password should not be blank..!")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain uppercase, lowercase, digit and special character"
    )
    private String password;

    @NotBlank(message = "Customer name please..")
    private String userName;

    @Email(message ="Customer emailId please..")
    private String email;

    private Set<String> roles;

}
