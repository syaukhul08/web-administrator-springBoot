package com.soalTest.k24.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "name", length = 225)
    private String name;

    @Column(name = "password", length = 20, unique = true)
    private String password;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "birth")
    private Date birth;

    @Column(name = "email", length = 225)
    private String email;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "ktp", length = 225)
    private String ktp;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    public void setPhoto(byte[] photoFile) {
        this.photo = photoFile;
    }

    public byte[] getPhoto() {
        return photo;
    }


}
