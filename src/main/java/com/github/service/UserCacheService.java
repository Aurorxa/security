package com.github.service;

import com.github.dto.LoginReturnDto;
import com.github.entity.User;

import java.security.InvalidKeyException;
import java.util.Optional;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-18 09:15:06
 */
public interface UserCacheService {

    /**
     * 将 User 存放到 Redis 中，返回 mfaId
     *
     * @param user
     * @return
     */
    String cacheUser(User user);


    /**
     * 从 Redis 中获取 Redis 对象
     *
     * @param mfaId
     * @return
     */
    Optional<User> extractUser(String mfaId);


    /**
     * 验证 totp
     *
     * @param mfaId
     * @param code
     * @return
     */
    Optional<LoginReturnDto> verifyTotp(String mfaId, String code) throws InvalidKeyException;

}
