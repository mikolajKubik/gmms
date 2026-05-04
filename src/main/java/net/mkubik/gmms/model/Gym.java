package net.mkubik.gmms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gym")
@AllArgsConstructor
@NoArgsConstructor
public class Gym extends BaseEntity {

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", unique = true, nullable = false, length = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$")
    @Size(max = 50)
    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;


}
