package com.quizz.AccountService.Entity.MySql;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userID;
    String name;
    String password;
    String email;
    String phone;

    @ManyToOne
    Role role;
}
