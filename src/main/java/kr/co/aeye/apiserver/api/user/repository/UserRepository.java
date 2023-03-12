package kr.co.aeye.apiserver.api.user.repository;

import kr.co.aeye.apiserver.api.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}
