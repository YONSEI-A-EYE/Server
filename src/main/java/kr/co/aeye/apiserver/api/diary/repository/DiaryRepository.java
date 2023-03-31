package kr.co.aeye.apiserver.api.diary.repository;

import kr.co.aeye.apiserver.api.diary.entity.Diary;
import kr.co.aeye.apiserver.api.diary.dto.diaryReport.EmotionHistogram;
import kr.co.aeye.apiserver.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findAll();
    Optional<Diary> findById(Long integer);
    boolean existsDiaryByUserAndDate(User user, LocalDate dateTime);
    Diary getById(Long id);

    Diary getDiaryByUserAndDate(User user, LocalDate datetime);
    List<Diary> findDiariesByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    @Query(name="find_emotion_histogram", nativeQuery = true)
    EmotionHistogram getEmotionHistogramByDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
