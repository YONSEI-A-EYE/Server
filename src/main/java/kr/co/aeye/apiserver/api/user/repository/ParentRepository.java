package kr.co.aeye.apiserver.api.user.repository;


import kr.co.aeye.apiserver.api.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends CrudRepository<User, Integer> {
        }
