package kr.co.aeye.apiserver.src.diary;

import org.springframework.data.repository.CrudRepository;

public interface DiaryRepository extends CrudRepository<Diary, Integer> {
}
