package com.github.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-18 14:46:37
 */
@Data
@Entity
@Table(name = "`permission`")
public class Permission implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 权限名称
     */
    @Column(name = "`permission_name`")
    private String authority;

    /**
     * 权限名称，显示名称
     */
    private String displayName;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();


    @Override
    public String getAuthority() {
        return authority;
    }
}
