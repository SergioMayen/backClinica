package com.example.auth.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 190) private String username;
    @Column(nullable = false) private String password;
    @Column(nullable = false) private int failedLoginAttempts;
    private Instant lockedUntil;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    protected AppUser() {}
    public AppUser(String username, String password, Role role) { this.username = username; this.password = password; this.roles.add(role); }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void updatePassword(String password) { this.password = password; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public Instant getLockedUntil() { return lockedUntil; }
    public void recordFailedLogin() { failedLoginAttempts++; }
    public void lockUntil(Instant value) { lockedUntil = value; }
    public void resetFailedLogins() { failedLoginAttempts = 0; lockedUntil = null; }
    public Set<Role> getRoles() { return roles; }
}
