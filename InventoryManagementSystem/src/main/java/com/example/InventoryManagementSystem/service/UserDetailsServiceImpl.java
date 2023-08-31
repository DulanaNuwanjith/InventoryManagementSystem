package com.example.InventoryManagementSystem.service;

import com.example.InventoryManagementSystem.dto.request.UpdateProfileRequest;
import com.example.InventoryManagementSystem.dto.response.UpdateProfileResponse;
import com.example.InventoryManagementSystem.model.User;
import com.example.InventoryManagementSystem.model.UserState;
import com.example.InventoryManagementSystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);
        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }
    @Transactional
    public void updateUserState(long userId, UserState newState) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setState(newState);
        userRepository.save(user);
    }
    public ResponseEntity<?> updateProfile(UpdateProfileRequest updateProfileRequest) {
        logger.info("Received request to update profile.");

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userDetails.getId()));

        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setPhoneno(updateProfileRequest.getPhoneno());

        userRepository.save(user);

        logger.info("Profile updated successfully for user: {}", userDetails.getUsername());

        return ResponseEntity.ok(new UpdateProfileResponse("Profile updated successfully"));
    }

}