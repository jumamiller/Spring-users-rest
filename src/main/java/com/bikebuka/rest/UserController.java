package com.bikebuka.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    /**
     * User repository
     */
    @Autowired
    private UserRepository userRepository;
    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("message", "You have successfully retrieved the list of users");
        body.put("data", users);
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body,HttpStatus.ACCEPTED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> show(@PathVariable Long id) {
        User user=userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("message", "You have successfully retrieved the list of users");
        body.put("data", user);
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }
    @PostMapping
    public User saveUser(@Validated @RequestBody User user) {
        return userRepository.save(user);
    }
    @PutMapping("/{id}")
    public Optional<User> update(@RequestBody User user, @PathVariable Long id) {
        return Optional.of(userRepository
                .findById(id)
                .map(el -> {
                    el.setName(user.getName());
                    el.setEmail(user.getEmail());
                    return userRepository.save(el);
                })
                .orElseGet(() -> {
                    user.setId(id);
                    return userRepository.save(user);
                }));
    }
    @DeleteMapping("/{id}")
    public void destroy(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
