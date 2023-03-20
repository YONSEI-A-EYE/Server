package kr.co.aeye.apiserver.api.user.repository;


import kr.co.aeye.apiserver.api.user.entity.Parent;
import kr.co.aeye.apiserver.api.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends CrudRepository<Parent, Integer> {
        Parent getParentByAuthCode(String authCode);
        Parent getParentByMainParent(Long id);
        Parent getParentBySubParent(Long id);
}
