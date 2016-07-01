package com.github.coderoute.users;

import com.github.coderoute.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User user) {
        UserEntity userEntity = new UserEntity(user.getName());
        userRepository.save(userEntity);
        user.setUuid(userEntity.getUuid());
        return user;
    }

    public User find(UUID userUuid) {
        UserEntity userEntity = userRepository.findOne(userUuid);
        if (userEntity == null) {
            throw new ResourceNotFoundException("No such user: " + userUuid);
        }
        return convertToUserFacade(userEntity);
    }

    private User convertToUserFacade(UserEntity userEntity) {
        User user = new User();
        user.setName(userEntity.getName());
        user.setUuid(userEntity.getUuid());
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll().stream().map(u -> convertToUserFacade(u)).collect(Collectors.toList());
    }

    public void delete(String userUuid) {
        userRepository.delete(UUID.fromString(userUuid));
    }
}
