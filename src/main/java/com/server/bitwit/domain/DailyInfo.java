package com.server.bitwit.domain;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class DailyInfo {
    
    LocalDate infoDate;
    
    Double openingPrice;
    
    Double closingPrice;
    
    Double minPrice;
    
    Double maxPrice;
    
    Double tradingVolume;
    
}
