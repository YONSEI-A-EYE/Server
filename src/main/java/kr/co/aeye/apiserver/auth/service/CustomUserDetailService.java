package kr.co.aeye.apiserver.auth.service;

import lombok.RequiredArgsConstructor;

import kr.co.aeye.apiserver.api.user.entity.CustomUserDetail;
import kr.co.aeye.apiserver.api.user.entity.User;
import kr.co.aeye.apiserver.api.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> tempUser = userRepository.findByEmail(email);
        if (tempUser.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        User user = tempUser.get();
        return  CustomUserDetail.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
