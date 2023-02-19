package kr.co.aeye.apiserver.src.diary.repository;

import kr.co.aeye.apiserver.src.diary.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    @Query(value = "select * from video where emotion=?1 order by rand() limit 1", nativeQuery = true)
    Video findVideoRandByTheme(String theme);
}
