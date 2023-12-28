package br.com.diameter.userservice.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(length = 200, nullable = false)
    private String name;

    @Column(length = 200, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
}
