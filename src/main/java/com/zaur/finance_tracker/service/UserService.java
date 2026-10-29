package com.zaur.finance_tracker.service;

import com.zaur.finance_tracker.model.User;
import com.zaur.finance_tracker.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
