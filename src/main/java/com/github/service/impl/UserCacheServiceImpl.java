package com.github.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.github.common.Constant;
import com.github.entity.User;
import com.github.service.UserCacheService;
import com.github.utils.TotpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Optional;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-18 09:16:00
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserCacheServiceImpl implements UserCacheService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public String cacheUser(User user) {
        String mfaId = RandomUtil.randomString(TotpUtil.PASSWORD_LENGTH);
        HashOperations<String, Object, Object> cache = redisTemplate.opsForHash();
        // 如果缓存中没有对应的用户数据，就将用户数据存放到缓存中，并设置缓存的 TTL
        if (!cache.hasKey(Constant.CACHE_MFA, mfaId)) {
            cache.put(Constant.CACHE_MFA, mfaId, user);
            redisTemplate.expire(Constant.CACHE_MFA, Duration.ofSeconds(TotpUtil.TIME_STEP));
        }
        return mfaId;
    }

    @Override
    public Optional<User> extractUser(String mfaId) {
        HashOperations<String, Object, Object> cache = redisTemplate.opsForHash();
        if (cache.hasKey(Constant.CACHE_MFA, mfaId)) {
            User user = (User) cache.get(Constant.CACHE_MFA, mfaId);
            return Optional.of(user);
        }
        return Optional.empty();
    }



}
