package com.xjudge.entity;

import com.xjudge.entity.key.UserContestKey;
import com.xjudge.model.enums.UserContestRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_contest")
public class UserContest {

    @EmbeddedId
    UserContestKey id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contest_id")
    @MapsId("contestId")
    private Contest contest;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    private Boolean isFavorite;

    @Enumerated(EnumType.STRING)
    private UserContestRole role;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserContest that = (UserContest) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
