package kr.co.aeye.apiserver.src.diary;

import kr.co.aeye.apiserver.src.diary.model.Diary;
import kr.co.aeye.apiserver.src.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    @Override
    List<Diary> findAll();

    @Override
    Optional<Diary> findById(Integer integer);

    boolean existsDiaryByUserAndDate(User user, LocalDate dateTime);
}
