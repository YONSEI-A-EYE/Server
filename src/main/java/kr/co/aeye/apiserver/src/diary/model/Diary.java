package kr.co.aeye.apiserver.src.diary.model;

import jakarta.persistence.*;
import kr.co.aeye.apiserver.src.user.models.User;
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
