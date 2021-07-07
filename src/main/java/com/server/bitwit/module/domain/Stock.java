package com.server.bitwit.module.domain;

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
public class Stock extends BaseTimeEntity
{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "stock_id")
    Long id;
    
    String name;
    
    public static Stock onlyId(Long id)
    {
        var stock = new Stock( );
        stock.id = id;
        return stock;
    }
    
    public static Stock createStock(String name)
    {
        var stock = new Stock( );
        stock.name = name;
        return stock;
    }
}
