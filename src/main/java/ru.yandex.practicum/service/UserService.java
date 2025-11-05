package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}