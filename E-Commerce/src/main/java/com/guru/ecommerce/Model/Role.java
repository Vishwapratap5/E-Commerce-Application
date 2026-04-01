package com.guru.ecommerce.Model;

import com.guru.ecommerce.Enums.AppRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @ToString.Exclude
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private AppRole roleName;


}
