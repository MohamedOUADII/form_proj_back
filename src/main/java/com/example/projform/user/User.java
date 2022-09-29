package com.example.projform.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "form_user")
public class User {
    @Id
    @SequenceGenerator(
            sequenceName = "sequence_user",
            initialValue = 1,
            name = "sequence_user"
    )
    @GeneratedValue(
            generator = "sequence_user",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    @Column(unique = true)
    private String code;
    private String last_name;
    private String first_name;
    private String email;
    private String phone;

    public User(String last_name,
                String first_name,
                String email,
                String phone) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone = phone;
    }

    public User(String code, String last_name, String first_name, String email, String phone) {
        this.code = code;
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone = phone;
    }

    public User(String email) {
        this.email = email;
        this.code = UUID.randomUUID().toString();
    }
}
