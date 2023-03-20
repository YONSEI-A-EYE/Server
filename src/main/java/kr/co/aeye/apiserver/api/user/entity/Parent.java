package kr.co.aeye.apiserver.api.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Parent {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="mainParent")
    private User mainParent;

    @ManyToOne
    @JoinColumn(name="subParent")
    private User subParent;

    @Column(length = 100)
    private String childName;

    @Column(length = 100)
    private String childTemperament;
}
