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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

        Optional<User> optional = userRepository.findOne(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("username"), "admin");
            }
        });
        // 如果数据库没有该账户
        if (!optional.isPresent()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRealName("系统管理员");
            user.setEmail("admin@qq.com");

            Role role = new Role();
            role.setAuthority("ADMIN");
            role.setAuthority("USER");

            user.getRoles().add(role);

            userRepository.save(user);
        }
    }
}
