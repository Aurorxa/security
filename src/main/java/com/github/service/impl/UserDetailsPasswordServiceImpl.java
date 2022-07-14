package com.github.service.impl;

import com.github.dao.UserRepository;
import com.github.entity.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 15:03:48
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

    @NonNull
    private UserRepository userRepository;

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {

        return userRepository
                .findOne((Specification<User>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), userDetails.getUsername()))
                .map(user -> {
                    user.setPassword(newPassword);
                    return (UserDetails) userRepository.save(user);
                }).orElse(userDetails);

    }
}
