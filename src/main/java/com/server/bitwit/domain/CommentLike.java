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
public class CommentLike {
    
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "commentLike_id")
    Long id;
    
    @ManyToOne(fetch = LAZY)
    Comment comment;
    
    @ManyToOne(fetch = LAZY)
    Account account;
    
    public CommentLike(Comment comment, Account account) {
        this.comment = comment;
        this.account = account;
    }
}
