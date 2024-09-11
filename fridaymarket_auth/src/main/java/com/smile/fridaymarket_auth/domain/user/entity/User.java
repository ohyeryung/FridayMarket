package com.smile.fridaymarket_auth.domain.user.entity;

import com.smile.fridaymarket_auth.domain.user.dto.UserUpdateRequest;
import com.smile.fridaymarket_auth.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_USER")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USER_ID", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "USERNAME", length = 20, nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "PHONE_NUMBER", length = 20, nullable = false)
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    @Column(name = "USER_ROLE", length = 20)
    @Builder.Default
    private Set<UserRole> userRole = new HashSet<>();

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;

    }

}
