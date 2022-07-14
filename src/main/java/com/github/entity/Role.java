package com.github.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 14:19:28
 */
@Data
@Entity
@Table(name = "`role`")
public class Role implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;


    @Override
    public String getAuthority() {
        return authority;
    }
}
