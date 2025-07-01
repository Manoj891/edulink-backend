package com.ms.ware.online.solution.school.config.security;

import com.ms.ware.online.solution.school.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class JwtHelper {
    private final String privateKey = "vpqoCz12Tx";

    public String create(String userId, String userName, String userType, String cashAccount, String context, String role) {
        String token = "";
        try {
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            token = Jwts.builder().
                    setId(userId)
                    .setIssuer(userName)
                    .setAudience(userType)
                    .setSubject(cashAccount)
                    .claim("context", context)
                    .claim("role", role) // ORG, STU, or TCR
                    .setIssuedAt(date)
                    .setExpiration(c.getTime())
                    .signWith(SignatureAlgorithm.HS256, privateKey).compact();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return token;
    }

    public AuthenticatedUser decodeToken(String authToken) {
        Claims claims = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(authToken.substring(7)).getBody();
        if (claims == null) {
            throw new UnauthorizedException();
        }
        return AuthenticatedUser.builder().context(claims.get("context").toString()).status(true).userId(claims.getId()).userType(claims.getAudience()).userName(claims.getIssuer()).cashAccount(claims.getSubject()).role(claims.get("role", String.class)).build();
    }
}