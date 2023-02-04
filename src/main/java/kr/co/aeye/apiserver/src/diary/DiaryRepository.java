package kr.co.aeye.apiserver.src.diary;

import kr.co.aeye.apiserver.src.diary.model.Diary;
import kr.co.aeye.apiserver.src.user.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends CrudRepository<Diary, Integer> {
    @Override
    List<Diary> findAll();

    @Override
    Optional<Diary> findById(Integer integer);
}
