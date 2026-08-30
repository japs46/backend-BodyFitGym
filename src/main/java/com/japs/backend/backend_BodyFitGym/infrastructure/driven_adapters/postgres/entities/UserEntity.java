package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@Entity(name = "gym_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 15)
    private String document;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false, length = 300)
    private String password;

    @Column(nullable = false)
    private String status;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @PrePersist
    protected void onCreate(){
        this.registrationDate = LocalDate.now();
        this.status = "Activo";
    }

    //private List<AfiliadoSimple> afiliados;
    //private List<ProductoSimple> productos;
    //private List<MembresiaSimple> membresias;

}
