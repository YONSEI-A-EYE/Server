package kr.co.aeye.apiserver.src.user;

import kr.co.aeye.apiserver.src.user.models.PostUserSignup;
import kr.co.aeye.apiserver.src.user.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAllUser(){
        List<User> users = userRepository.findAll();

        log.info("find all users. users={}", users);
        return users;
    }

    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable int userId){
        Optional<User> reqUser = userRepository.findById(userId);

        log.info("find user by id. reqUser={}", reqUser);
        return reqUser;
    }

    @PostMapping("/signup")
    public User signUp (@RequestBody PostUserSignup postUserSignup){
        User newUser = User.builder()
                .name(postUserSignup.getName())
                .email(postUserSignup.getEmail())
                .password(postUserSignup.getPassword())
                .build();

        log.info("sign up new user. newUser={}", newUser);
        return userRepository.save(newUser);
    }
}
