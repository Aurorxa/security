package com.github.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 06:55:30
 */
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    @Getter
    @Setter
    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {
        /**
         * 访问令牌的过期时间
         */
        private Long accessTokenExpireTime = 60 * 1000L;
        /**
         * 刷新令牌的过期时间
         */
        private Long refreshTokenExpireTime = 30 * 24 * 3600 * 1000L;
    }

}
