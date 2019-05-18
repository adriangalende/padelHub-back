package com.adriangalende.padelHub.security;

import com.adriangalende.padelHub.model.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    public String generate(JwtUser jwtUser){
        Claims claims = Jwts.claims().setSubject(jwtUser.getUsername());
        claims.put("userId",String.valueOf(jwtUser.getId()));
        claims.put("clubId",String.valueOf(jwtUser.getIdClub()));
        claims.put("role",String.valueOf(jwtUser.getRole()));
        claims.put("email",String.valueOf(jwtUser.getEmail()));

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256,"65b2217b4588464b3d3fc50d794b03a939bed83100668a625e2e664de719e0cf" ).compact();
    }

}

