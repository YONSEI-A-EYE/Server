package kr.co.aeye.apiserver.api.diary.repository;

import kr.co.aeye.apiserver.api.diary.entity.Comment;
import kr.co.aeye.apiserver.api.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Long countCommentsByDiary(Diary diary);

    List<Comment> getAllByDiaryOrderById(Diary diary);
}
