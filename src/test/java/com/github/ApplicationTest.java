package com.github;

import com.github.entity.Role;
import com.github.entity.User;
import com.github.utils.JwtUtil;
import com.github.utils.TotpUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.InvalidKeyException;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 20:35:30
 */
@Slf4j
@RequiredArgsConstructor
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TotpUtil totpUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test() {

        log.info("{}", passwordEncoder);

        User user = new User();
        user.setUsername("admin");
        user.setPhone("17722291552");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRealName("系统管理员");
        user.setEmail("admin@qq.com");

        Role role = new Role();
        role.setAuthority("ADMIN");
        role.setAuthority("USER");

        user.getRoles().add(role);

        String jwtToken = jwtUtil.createAccessToken(user);
        log.info("jwtToken == {}", jwtToken);

        Claims claims = Jwts.parserBuilder().setSigningKey(JwtUtil.ACCESS_KEY).build().parseClaimsJws(jwtToken).getBody();
        log.info("claims == {}", claims);
        log.info("subject == {}", claims.getSubject());

    }


    @Test
    public void testTotp() throws InvalidKeyException {
        Key key = totpUtil.generateKey();
        Instant now = Instant.now();
        String first = totpUtil.createTotp(key, now);
        System.out.println("first = " + first);
        System.out.println("first boolean = " + totpUtil.verifyTotp(key, first));

        Instant later = now.plus(Duration.ofSeconds(TotpUtil.TIME_STEP));
        String second = totpUtil.createTotp(key, later);
        System.out.println("second = " + second);
        System.out.println("second boolean = " + totpUtil.verifyTotp(key, second));
    }

    @Test
    public void test3() {
        Key key = totpUtil.generateKey();
        String str = totpUtil.encodeKeyToString(key);
        Key key1 = totpUtil.decodeKeyFromString(str);
        System.out.println("key = " + key);
        System.out.println("key1 = " + key1);
        System.out.println("str = " + str);
        System.out.println("boolean = " + (key == key1));
    }


}
