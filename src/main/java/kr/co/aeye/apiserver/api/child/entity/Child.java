package kr.co.aeye.apiserver.api.child.entity;


import jakarta.persistence.*;
import kr.co.aeye.apiserver.api.user.entity.Parent;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Child {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String childName;

    @Column
    private int childAge;

    @Column(length = 100)
    private String childTemperament;

    @ManyToOne
    @JoinColumn(name="parent", referencedColumnName = "id")
    private Parent parent;
}
