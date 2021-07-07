package com.server.bitwit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Account extends BaseTimeEntity
{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "account_id")
    Long id;
    
    String name;
    
    String email;
    
    String password;
    
    public static Account createAccount(String name, String email, String password)
    {
        var account = new Account( );
        account.name     = name;
        account.email    = email;
        account.password = password;
        return account;
    }
    
    public static Account onlyId(Long id)
    {
        var account = new Account( );
        account.id = id;
        return account;
    }
}
