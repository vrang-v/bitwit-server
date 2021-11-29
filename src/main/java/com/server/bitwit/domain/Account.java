package com.server.bitwit.domain;

import com.server.bitwit.module.common.converter.AuthorityListJsonConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor
@Entity
public class Account extends BaseTimeEntity {
    
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "account_id")
    Long id;
    
    String name;
    
    String email;
    
    String password;
    
    boolean emailVerified;
    
    String emailToken;
    
    LocalDateTime lastEmailTokenGeneratedAt;
    
    LocalDateTime joinedAt;
    
    @ManyToMany
    List<Stock> stocks;
    
    @Convert(converter = AuthorityListJsonConverter.class)
    List<Authority> authorities;
    
    @Enumerated(EnumType.STRING)
    AccountType accountType;
    
    public static Account createEmailAccount(String name, String email, String password) {
        var encodedPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder( ).encode(password);
        var account         = new Account( );
        account.name        = name;
        account.email       = email;
        account.password    = encodedPassword;
        account.authorities = List.of(Authority.USER);
        account.accountType = AccountType.EMAIL;
        return account;
    }
    
    public static Account createOAuthAccount(String name, String email, AccountType accountType) {
        var account = new Account( );
        account.name          = name;
        account.email         = email;
        account.authorities   = List.of(Authority.USER);
        account.accountType   = accountType;
        account.emailVerified = true;
        account.joinedAt      = LocalDateTime.now( );
        return account;
    }
    
    public Account changeName(String name) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
        return this;
    }
    
    public Account changePassword(String password) {
        if (StringUtils.hasText(password)) {
            this.password = PasswordEncoderFactories.createDelegatingPasswordEncoder( ).encode(password);
        }
        return this;
    }
    
    public Account changeEmail(String email) {
        if (StringUtils.hasText(email)) {
            this.email = email;
        }
        return this;
    }
    
    public Account generateEmailToken( ) {
        this.emailToken                = UUID.randomUUID( ).toString( );
        this.lastEmailTokenGeneratedAt = LocalDateTime.now( );
        return this;
    }
    
    public Account verify( ) {
        this.joinedAt      = LocalDateTime.now( );
        this.emailVerified = true;
        this.emailToken    = null;
        return this;
    }
}
