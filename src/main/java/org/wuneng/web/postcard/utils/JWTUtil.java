package org.wuneng.web.postcard.utils;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.bcel.Const;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.Constant;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

public class JWTUtil {

    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(Constant.JWT_SECERT);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    /**
     *
     * @param id            token的id
     * @param subject
     * @param ttlMillis     超时/ms
     * @return
     */
    public static String createJWT(String id, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, secretKey);
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate);
        }
        return builder.compact();
    }

    /**
     * 验证JWT
     * @param jwtStr
     * @return
     */
    public static CheckResult validateJWT(String jwtStr) {
        CheckResult checkResult = new CheckResult();
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr);
            Date date = claims.getExpiration();
            if (date!=null) {
                if (date.after(new Date(System.currentTimeMillis()))) {
                    checkResult.setSuccess(true);
                    checkResult.setPayload(claims);
                }else {
                    checkResult.setSuccess(false);
                    checkResult.setErrCode("time out");
                }
            }else {
                checkResult.setSuccess(true);
                checkResult.setPayload(claims);
            }
        } catch (ExpiredJwtException e) {
            checkResult.setErrCode(Constant.JWT_ERRCODE_EXPIRE);
            checkResult.setSuccess(false);
        } catch (SignatureException e) {
            checkResult.setErrCode(Constant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        } catch (Exception e) {
            checkResult.setErrCode(Constant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        }
        return checkResult;
    }


    public static   Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
