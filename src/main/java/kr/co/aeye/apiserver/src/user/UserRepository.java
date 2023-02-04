package kr.co.aeye.apiserver.src.user;

import kr.co.aeye.apiserver.src.user.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Override
    List<User> findAll();

    @Override
    Optional<User> findById(Integer integer);
}
