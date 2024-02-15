package com.sampledashboard1.config.security;

import com.sampledashboard1.model.Login;
import com.sampledashboard1.utils.MethodUtils;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class CustomUser extends Login implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String email;

    private String password;

    private String firstName;

    private String roleType;
    private String currentUserId;

    private Collection<? extends GrantedAuthority> authorities;

    public CustomUser(Long id, String email, String password, String name, String currentUserId, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = name;
        this.currentUserId = currentUserId;
        this.authorities = authorities;
    }

    public static CustomUser build(Login login) {
        Set<GrantedAuthority> authorities = new HashSet<>();
       // authorities.add(new SimpleGrantedAuthority(login.getRole().getName()));
        String username = "";
        String userId = "";
        if (!MethodUtils.isObjectisNullOrEmpty(login.getUsers())) {
            username = login.getUsers().getFirstName();
            userId = login.getUsers().getId().toString();
        }
        return new CustomUser(login.getId(), login.getEmail(), login.getPassword(), username, userId, authorities);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return id.toString();
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getName() {
        return firstName;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}
