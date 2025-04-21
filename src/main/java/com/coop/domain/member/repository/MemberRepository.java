package com.coop.domain.member.repository;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.id IN :memberIds")
    List<Member> findMembersByIds(@Param("memberIds") Set<Long> memberIds);

    @Query("select m.id from Member m where m.role = :role")
    List<Long> findIdsByRole(@Param("role") Role role);
}
