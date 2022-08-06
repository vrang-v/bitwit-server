package com.server.bitwit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {
    
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    Long id;
    
    String content;
    
    int depth;
    
    boolean deleted;
    
    boolean edited;
    
    @ManyToOne(fetch = LAZY)
    Account writer;
    
    @ManyToOne(fetch = LAZY)
    Post post;
    
    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    Comment parent;
    
    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Comment> children = new ArrayList<>( );
    
    @BatchSize(size = 500)
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    Set<CommentLike> likes = new HashSet<>( );
    
    public Comment(String content, Account writer, Post post, Comment parent) {
        this.content = content;
        this.writer  = writer;
        this.post    = post;
        this.parent  = parent;
        this.depth   = parent != null ? parent.getDepth( ) + 1 : 0;
    }
    
    public String getContent( ) {
        return deleted ? "삭제된 댓글입니다." : content;
    }
    
    public Comment updateContent(String content) {
        this.content = content;
        this.edited  = true;
        return this;
    }
    
    public Comment delete( ) {
        this.deleted = true;
        return this;
    }
    
    public Comment getDeletableTopLevelComment( ) {
        if (parent != null && parent.getChildren( ).size( ) == 1 && parent.isDeleted( )) {
            return parent.getDeletableTopLevelComment( );
        }
        return this;
    }
    
    public boolean isRoot( ) {
        return parent == null;
    }
}