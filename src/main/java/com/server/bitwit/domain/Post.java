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

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Post extends BaseTimeEntity {
    
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    Long id;
    
    String title;
    
    @Column(columnDefinition="TEXT")
    String content;
    
    int viewCount;
    
    boolean edited;
    
    @ManyToOne(fetch = FetchType.LAZY)
    Account writer;
    
    @ManyToMany
    List<Tag> tags = new ArrayList<>( );
    
    @ManyToMany
    Set<Stock> stocks = new HashSet<>( );
    
    @BatchSize(size = 500)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    Set<PostLike> likes = new HashSet<>( );
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>( );
    
    public Post(String title, String content, Account writer) {
        this.title   = title;
        this.content = content;
        this.writer  = writer;
    }
    
    public Post clearTag( ) {
        this.tags.clear();
        return this;
    }
    
    public Post addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }
    
    public Post addStock(Stock stock) {
        this.stocks.add(stock);
        return this;
    }
    
    public Post plusViewCount( ) {
        viewCount += 1;
        return this;
    }
    
    public Post updateTitle(String title) {
        this.title  = title;
        this.edited = true;
        return this;
    }
    
    public Post updateContent(String content) {
        this.content = content;
        this.edited  = true;
        return this;
    }
    
    public Post updateStocks(Set<Stock> stocks) {
        this.stocks = stocks;
        this.edited = true;
        return this;
    }
}
