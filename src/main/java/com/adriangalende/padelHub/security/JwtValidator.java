package com.adriangalende.padelHub.security;

import com.adriangalende.padelHub.model.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String secretKey = "65b2217b4588464b3d3fc50d794b03a939bed83100668a625e2e664de719e0cf";

    public JwtUser validate(String token) {

        JwtUser jwtUser = null;

        try{
            Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

            jwtUser = new JwtUser();

            jwtUser.setUsername(body.getSubject());
            jwtUser.setId(Long.parseLong((String)body.get("userId")));
            jwtUser.setIdClub(Long.parseLong((String)body.get("clubId")));
            jwtUser.setRole((String) body.get("role"));
            jwtUser.setEmail((String) body.get("email"));

        }catch (Exception e){
            LOGGER.error("jwtUser validate ha fallado", e);
        }


        return jwtUser;
    }
}
