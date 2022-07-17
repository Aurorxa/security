package com.github.init;

import com.github.dao.UserRepository;
import com.github.entity.Role;
import com.github.entity.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 14:48:12
 */
@Component
@RequiredArgsConstructor
public class DefaultApplicationRunner implements ApplicationRunner {

    @NonNull
    private UserRepository userRepository;
    @NonNull
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Optional<User> optional = userRepository.findOne((Specification<User>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), "admin"));
        // 如果数据库没有该账户
        if (!optional.isPresent()) {
            User user = new User();
            user.setUsername("admin");
            user.setPhone("17722291552");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRealName("系统管理员");
            user.setEmail("admin@qq.com");

            Role adminRole = new Role();
            adminRole.setAuthority("ROLE_ADMIN");
            Role userRole = new Role();
            userRole.setAuthority("ROLE_USER");

            user.getRoles().add(adminRole);
            user.getRoles().add(userRole);

            userRepository.save(user);
        }
    }
}
