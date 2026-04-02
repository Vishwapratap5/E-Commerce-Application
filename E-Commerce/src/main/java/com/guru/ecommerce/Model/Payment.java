package com.guru.ecommerce.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "payements")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment",cascade={CascadeType.MERGE,CascadeType.PERSIST})
    private Order order;

    @NotBlank
    @Size(min = 4,message = "payment must contains at least 4 characters")
    private String paymentMethod;

    private String pgPaymentId;

    private String pgStatus;

    private String pgResponseMessage;

    private String pgName;

    //this constructor helps us to create a temporary payment in project
    public Payment(String paymentMethod,String pgPaymentId,String pgStatus,String pgResponseMessage,String pgName) {
        this.paymentMethod=paymentMethod;
        this.pgPaymentId=pgPaymentId;
        this.pgStatus=pgStatus;
        this.pgResponseMessage=pgResponseMessage;
        this.pgName=pgName;
    }

}
