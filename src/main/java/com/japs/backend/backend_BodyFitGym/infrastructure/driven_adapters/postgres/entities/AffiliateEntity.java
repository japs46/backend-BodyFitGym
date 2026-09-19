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
@Entity(name = "affiliate")
public class AffiliateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sin "unique"/"nullable = false" a propósito: la migración de datos del sistema
    // legado (sin validaciones) debe poder cargar filas incompletas o con
    // identificaciones duplicadas sin que la base de datos las rechace. La
    // integridad de lo que entra por la API la garantiza la validación de Affiliate
    // (Bean Validation) y la regla de identificación única en el caso de uso.
    @Column
    private String identification;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "second_last_name")
    private String secondLastName;

    @Column
    private String sex;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "affiliation_date")
    private LocalDate affiliationDate;

    @Column
    private String address;

    @Column
    private String neighborhood;

    @Column
    private String city;

    @Column
    private String status;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column
    private String email;

    @Column
    private String occupation;

    @Column
    private Double weight;

    @Column
    private Double height;

    @Column
    private Double waist;

    @Column
    private Double leg;

    @Column
    private Double hip;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = "Activo";
        }
        if (this.affiliationDate == null) {
            this.affiliationDate = LocalDate.now();
        }
    }
}
