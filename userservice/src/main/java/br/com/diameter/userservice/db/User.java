package br.com.diameter.userservice.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    @NotBlank(message = "Name must not be empty")
    private String name;

    @Column
    @Email(message = "E-mail is not valid")
    @NotBlank(message = "E-mail must not be empty")
    @Size(min = 10, max = 200, message = "E-mail must be between {min} and {max} characters")
    private String email;

    @Column
    @NotBlank(message = "Password must not be empty")
    private String password;
}
