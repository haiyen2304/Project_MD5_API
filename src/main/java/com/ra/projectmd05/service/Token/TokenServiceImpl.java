package com.ra.projectmd05.service.Token;

import com.ra.projectmd05.exception.UnauthorizedAccessException;
import com.ra.projectmd05.model.entity.Token;
import com.ra.projectmd05.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    // Vô hiệu hóa một token bằng cách lưu vào cơ sở dữ liệu.
    @Override
    public void invalidateToken(String tokenValue) {
        Token token = new Token();
        token.setTokenValue(tokenValue);
        token.setInvalidated(true);
        tokenRepository.save(token); // luu danh sach blackList
    }

    // Kiểm tra xem token có bị vô hiệu hóa không.
    @Override
    public boolean isTokenInvalidated(String tokenValue) {
        boolean isInvalidated = tokenRepository.findByTokenValue(tokenValue)
                .map(Token::isInvalidated)
                .orElse(false);
        if (isInvalidated) {
            throw new UnauthorizedAccessException("Token đã bị vô hiệu hóa, vui lòng đăng nhập lại");
        }
        return false;
    }


}
