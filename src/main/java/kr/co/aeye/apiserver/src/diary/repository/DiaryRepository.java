package kr.co.aeye.apiserver.src.diary.repository;

import kr.co.aeye.apiserver.src.diary.model.Diary;
import kr.co.aeye.apiserver.src.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findAll();
    Optional<Diary> findById(Long integer);
    boolean existsDiaryByUserAndDate(User user, LocalDate dateTime);
    List<Diary> findDiariesByDateBetween(LocalDate startDate, LocalDate endDate);
}
