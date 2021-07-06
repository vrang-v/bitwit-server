package com.server.bitwit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Ballot
{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "voter_id")
    Long id;
    
    @ManyToOne(fetch = LAZY)
    Account account;
    
    @Enumerated(STRING)
    VotingOption votingOption;
    
    @CreatedDate
    Instant createdAt;
    
    @LastModifiedDate
    Instant updatedAt;
}
