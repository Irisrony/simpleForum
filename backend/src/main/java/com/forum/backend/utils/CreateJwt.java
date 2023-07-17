package com.forum.backend.utils;

import com.forum.backend.domain.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwt {
    public static String getToken(User user){
        Jwts.builder();
        Jwts.parser();

        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(user.getId()+"")
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"MY-SECRET")
                .setExpiration(new Date(new Date().getTime()+86400000));
        return jwtBuilder.compact();
    }
}
