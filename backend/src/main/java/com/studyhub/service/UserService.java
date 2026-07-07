package com.studyhub.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studyhub.config.JwtUtil;
import com.studyhub.entity.User;
import com.studyhub.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public String register(String username, String password, String email) {
        if (username == null || username.isEmpty()) throw new RuntimeException("用户名不能为空");
        if (password == null || password.length() < 6) throw new RuntimeException("密码至少6位");
        User exist = baseMapper.selectByUsername(username);
        if (exist != null) throw new RuntimeException("用户名已被注册");
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("USER");
        user.setPoints(100);
        save(user);
        return jwtUtil.generateToken(user.getId(), username, "USER");
    }

    public String login(String username, String password) {
        User user = baseMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword()))
            throw new RuntimeException("用户名或密码错误");
        if ("FROZEN".equals(user.getStatus()))
            throw new RuntimeException("账号已被冻结");
        return jwtUtil.generateToken(user.getId(), username, user.getRole());
    }

    public User getCurrentUser(Long userId) {
        User user = getById(userId);
        if (user != null) user.setPassword(null);
        return user;
    }

    @Transactional
    public void updateInfo(Long userId, String email, String studentId) {
        User user = getById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        if (email != null && !email.isEmpty() && !email.equals(user.getEmail())) {
            User exist = baseMapper.selectByEmail(email);
            if (exist != null) throw new RuntimeException("邮箱已被使用");
            user.setEmail(email);
        }
        if (studentId != null) {
            user.setStudentId(studentId);
        }
        updateById(user);
    }

    @Transactional
    public void updateAvatar(Long userId, String avatarUrl) {
        User user = getById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        user.setAvatar(avatarUrl);
        updateById(user);
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new RuntimeException("原密码错误");
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
    }
}
