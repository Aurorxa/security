package com.github.init;

import com.github.dao.PermissionRepository;
import com.github.dao.RoleRepository;
import com.github.dao.UserRepository;
import com.github.entity.Permission;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 14:48:12
 */
@Component
@RequiredArgsConstructor
public class InitApplicationRunner implements ApplicationRunner {

    @NonNull
    private UserRepository userRepository;
    @NonNull
    private PasswordEncoder passwordEncoder;
    @NonNull
    private RoleRepository roleRepository;
    @NonNull
    private PermissionRepository permissionRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Permission permission1 = new Permission();
        permission1.setDisplayName("查询用户信息");
        permission1.setAuthority("USER_READ");


        Permission permission2 = new Permission();
        permission2.setDisplayName("新建用户信息");
        permission2.setAuthority("USER_CREATE");


        Permission permission3 = new Permission();
        permission3.setDisplayName("编辑用户信息");
        permission3.setAuthority("USER_UPDATE");

        Permission permission4 = new Permission();
        permission4.setDisplayName("USER_ADMIN");
        permission4.setAuthority("用户管理");




    }
}
