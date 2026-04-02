package com.guru.ecommerce.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "users",uniqueConstraints ={ @UniqueConstraint(columnNames = "username"),
                                           @UniqueConstraint(columnNames = "email") })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Size(min = 1, max = 20)
    private String username;

    @NotBlank
    @Size(min = 1, max = 20)
    private String password;

    @NotBlank
    @Email
    @Size(min = 1, max = 50)
    private String email;

    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER, cascade ={ CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name ="user_role",joinColumns = @JoinColumn(name ="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles=new HashSet<>();


    @OneToMany(mappedBy = "user",cascade ={ CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval=true)
    @ToString.Exclude
    private Set<Product> products;


    @Getter
    @Setter
    @OneToMany(mappedBy = "user",cascade ={CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true)
    private List<Address> addresses=new ArrayList<>();


    public User(String user1, String mail, String password1) {
        this.username = user1;
        this.password = password1;
        this.email = mail;
    }

    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade ={ CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval=true)
    private Cart cart;
}
