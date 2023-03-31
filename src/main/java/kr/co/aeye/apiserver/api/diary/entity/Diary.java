package kr.co.aeye.apiserver.api.diary.entity;

import jakarta.persistence.*;
import kr.co.aeye.apiserver.api.diary.dto.diaryReport.EmotionHistogram;
import kr.co.aeye.apiserver.api.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@NamedNativeQuery(
        name="find_emotion_histogram",
        query =
                "select count(case when emotion like 'excited%' then 1 end)       as excited,\n" +
                        "       count(case when emotion like 'happy%' then 1 end)         as happy,\n" +
                        "       count(case when emotion like 'calm%' then 1 end)          as calm,\n" +
                        "       count(case when emotion like 'content%' then 1 end)       as content,\n" +
                        "       count(case when emotion like 'anticipate%' then 1 end)    as anticipate,\n" +
                        "       count(case when emotion like 'tense%' then 1 end)         as tense,\n" +
                        "       count(case when emotion like 'angry%' then 1 end)         as angry,\n" +
                        "       count(case when emotion like 'sad%' then 1 end)           as sad,\n" +
                        "       count(case when emotion like 'badSurprised%' then 1 end)  as badSurprised,\n" +
                        "       count(case when emotion like 'goodSurprised%' then 1 end) as goodSurprised,\n" +
                        "       count(case when emotion like 'relaxed%' then 1 end)       as relaxed,\n" +
                        "       count(case when emotion like 'bored%' then 1 end)         as bored,\n" +
                        "       count(case when emotion like 'tired%' then 1 end)         as tired\n" +
                        "from diary\n" +
                        "where user=:userId AND date between :startDate AND :endDate",
        resultSetMapping = "emotion_histogram_dto"
)
@SqlResultSetMapping(
        name = "emotion_histogram_dto",
        classes = @ConstructorResult(
                targetClass = EmotionHistogram.class,
                columns = {
                        @ColumnResult(name="excited", type=int.class),
                        @ColumnResult(name="happy", type=int.class),
                        @ColumnResult(name="calm", type=int.class),
                        @ColumnResult(name="content", type=int.class),
                        @ColumnResult(name="anticipate", type=int.class),
                        @ColumnResult(name="tense", type=int.class),
                        @ColumnResult(name="angry", type=int.class),
                        @ColumnResult(name="sad", type=int.class),
                        @ColumnResult(name="badSurprised", type=int.class),
                        @ColumnResult(name="goodSurprised", type=int.class),
                        @ColumnResult(name="relaxed", type=int.class),
                        @ColumnResult(name="bored", type=int.class),
                        @ColumnResult(name="tired", type=int.class)
                }
        )

)
public class Diary {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name="video", nullable = true)
    private Video video;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = true, length = 100)
    private String emotion;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private Float score;

    @Column(nullable = true)
    private Float magnitude;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
