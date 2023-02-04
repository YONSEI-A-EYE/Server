package kr.co.aeye.apiserver.src.diary;

import jakarta.persistence.*;
import kr.co.aeye.apiserver.src.user.models.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Diary {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user")
    private User user;

    @Column(nullable = false)
    private LocalDateTime date;

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
