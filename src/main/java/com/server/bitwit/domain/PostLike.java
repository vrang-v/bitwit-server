package com.server.bitwit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class PostLike {
    
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "like_id")
    Long id;
    
    @ManyToOne(fetch = LAZY)
    Account account;
    
    @ManyToOne(fetch = LAZY)
    Post post;
    
    public PostLike(Account account, Post post) {
        this.account = account;
        this.post    = post;
    }
}
