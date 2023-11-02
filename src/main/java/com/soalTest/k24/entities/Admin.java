package com.soalTest.k24.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "admin")
public class Admin {

    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "name", length = 225)
    private String name;

    @Column(name = "password", length = 20, unique = true)
    private String password;

}
