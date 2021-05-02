package com.mschabow.flyrecipeserver.service;

import com.mschabow.flyrecipeserver.domain.User;
import com.mschabow.flyrecipeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> list() {return userRepository.findAll();}

    public User save(User user){
        return userRepository.save(user);
    }

    public Iterable<User> save(List<User> users){
        return userRepository.saveAll(users);
    }

    public boolean existsById(String id){
        return userRepository.existsById((id));
    }

    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }
}


