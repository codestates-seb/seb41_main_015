package com.book.village.server.domain.member.entity;

import com.book.village.server.global.audit.Auditable;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false,updatable = false, unique = true, length = 100)
    private String email;

    @Column(length = 30)
    private String name;

    @Column(unique = true, length = 20)
    @Size(min = 2, max = 20)
    private String displayName;

    @Column(length=3000)
    private String imgUrl="https://img.icons8.com/windows/32/null/user-male-circle.png";

    @ColumnDefault("''")
    private String address;

    @Column(length = 30)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }

    public Member(String email) {
        this.email = email;
    }
}
