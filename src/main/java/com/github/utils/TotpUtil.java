package com.github.utils;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

/**
 * TimeBasedOneTimePassword 工具类
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-17 20:47:58
 */
@Component
@Slf4j
public class TotpUtil {

    /**
     * 多少秒之内保持不变
     */
    public static final long TIME_STEP = 60 * 5L;

    /**
     * 密码的长度
     */
    public static final int PASSWORD_LENGTH = 6;


    private TimeBasedOneTimePasswordGenerator totp;

    private KeyGenerator keyGenerator;

    {
        try {
            totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(TIME_STEP), PASSWORD_LENGTH);
            keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
            keyGenerator.init(512);
        } catch (NoSuchAlgorithmException e) {
            log.error("TotpUtil {}", e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 创建 Totp
     *
     * @param key
     * @param time
     * @return
     * @throws InvalidKeyException
     */
    public String createTotp(Key key, Instant time) throws InvalidKeyException {
        return totp.generateOneTimePasswordString(key, time);
    }

    /**
     * 验证 totp
     *
     * @param key
     * @param code
     * @return
     * @throws InvalidKeyException
     */
    public boolean verifyTotp(Key key, String code) throws InvalidKeyException {

        return code.equals(createTotp(key, Instant.now()));
    }

    /**
     * 获取 key ，每个用户的 key 应该不一样
     *
     * @return
     */
    public Key generateKey() {
        return keyGenerator.generateKey();
    }

    /**
     * 将 key 转为 Base64 编码
     *
     * @param key
     * @return
     */
    public String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 将 Key 转为 Base64 编码
     *
     * @return
     */
    public String encodeKeyToString() {
        return encodeKeyToString(generateKey());
    }

    /**
     * 将 Base64 编码转换为 Key
     *
     * @param keyStr
     * @return
     */
    public Key decodeKeyFromString(String keyStr) {
        return new SecretKeySpec(Base64.getDecoder().decode(keyStr), totp.getAlgorithm());
    }


}
