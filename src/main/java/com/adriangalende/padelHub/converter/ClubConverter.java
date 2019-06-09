package com.adriangalende.padelHub.converter;

import com.adriangalende.padelHub.entity.ClubEntity;
import com.adriangalende.padelHub.model.Club;
import org.springframework.stereotype.Component;

@Component("convertidor_club")
public class ClubConverter {

    public Club convertirEntity(ClubEntity clubEntity){
        return new Club(clubEntity);
    }

}
