package com.github.service.impl;

import com.github.common.Result;
import com.github.dao.UserRepository;
import com.github.dto.UserDto;
import com.github.entity.User;
import com.github.ex.BizException;
import com.github.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 16:27:08
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @NonNull
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User findByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);

        return userRepository.findOne(Example.of(user, ExampleMatcher.matching().withMatcher("phone", ExampleMatcher.GenericPropertyMatchers.exact()))).orElse(null);
    }

    @Override
    public boolean checkUsernameExisted(String username) {
        return userRepository.count((Specification<User>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username)) > 0;
    }

    @Override
    public boolean checkEmailExisted(String email) {
        return userRepository.count((Specification<User>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email)) > 0;
    }

    @Override
    public boolean checkMobileExisted(String mobile) {
        return userRepository.count((Specification<User>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phone"), mobile)) > 0;
    }

    @Override
    public Result<?> register(UserDto userDto) {
        // 1：确保 username、email、mobile 唯一，需要去数据库中校验
        if (this.checkUsernameExisted(userDto.getUsername())) {
            throw new BizException("用户名已经存在");
        }
        if (this.checkMobileExisted(userDto.getMobile())) {
            throw new BizException("手机号码已经存在");
        }
        if (this.checkEmailExisted(userDto.getEmail())) {
            throw new BizException("邮箱已经存在");
        }

        // 2. 我们需要将 userDto 转换为 user ，并分配一个默认的角色（ROLE_USER）
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRealName(userDto.getRealName());
        user.setEmail(userDto.getEmail());
        user.setNickName(userDto.getNickName());
        user.setPhone(userDto.getMobile());

        userRepository.save(user);


        return Result.success();
    }


}
