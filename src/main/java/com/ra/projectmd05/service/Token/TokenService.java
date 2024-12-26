package com.ra.projectmd05.service.Token;

public interface TokenService {
    void invalidateToken(String tokenValue);
    boolean isTokenInvalidated(String tokenValue);
}
