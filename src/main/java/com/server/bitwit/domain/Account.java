package com.server.bitwit.domain;

import com.server.bitwit.module.common.converter.AuthorityListJsonConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Account extends BaseTimeEntity {
    
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "account_id")
    Long id;
    
    String name;
    
    String email;
    
    String password;
    
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
        account.name        = name;
        account.email       = email;
        account.authorities = List.of(Authority.USER);
        account.accountType = accountType;
        return account;
    }
    
    public Account changeName(String name) {
        if (name != null) {
            this.name = name;
        }
        return this;
    }
    
    public Account changePassword(String password) {
        if (password != null) {
            this.password = PasswordEncoderFactories.createDelegatingPasswordEncoder( ).encode(password);
        }
        return this;
    }
    
    public Account changeEmail(String email) {
        if (email != null) {
            this.email = email;
        }
        return this;
    }
}
