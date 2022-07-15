package com.github;

import com.github.entity.Role;
import com.github.entity.User;
import com.github.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

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

        Claims claims = Jwts.parserBuilder().setSigningKey(JwtUtil.KEY).build().parseClaimsJws(jwtToken).getBody();
        log.info("claims == {}", claims);
        log.info("subject == {}", claims.getSubject());

    }

}
