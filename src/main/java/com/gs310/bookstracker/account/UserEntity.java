package com.gs310.bookstracker.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
public class UserEntity {

    @Id
    @Column(name = "user_id")
    // if this needs to generated by JPA, we need to let it know.
    // here, during creation of the table, we have mentioned SERIAL, asking postgres to take care of generating IDs.
    @GeneratedValue(strategy = GenerationType.AUTO) // this says not to generate, as DB is going to take care
    private Long userId; // TODO: lowercase.

    @Column(unique = true)
    private String username; 

    @Column(unique = true)
    private String email; 

    private String password;
    private String role;

    // private boolean isVerified; // for password recovery purposes

}
