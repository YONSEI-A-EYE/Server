package kr.co.aeye.apiserver.api.child.repository;

import kr.co.aeye.apiserver.api.child.entity.Child;
import kr.co.aeye.apiserver.api.user.entity.Parent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends CrudRepository<Child, Integer> {
    List<Child> getChildrenByParent(Parent parent);

    Child getChildById(Long childId);
}
