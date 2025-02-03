package com.hongik.url.user.domain.entity;

import com.hongik.url.common.Inheritance.BaseEntity;
import com.hongik.url.user.domain.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
@SQLDelete(sql="UPDATE users set deleted = true where user_id = ?")
@Where(clause = "deleted = false")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String username, String password, Role role){
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void changeRole(){
        if(this.role == Role.ADMIN){
            this.role = Role.GENERAL;
        } else{
            this.role = Role.ADMIN;
        }
    }
}
