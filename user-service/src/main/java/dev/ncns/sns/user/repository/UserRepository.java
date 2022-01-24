package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByNickname(String nickname);
    Optional<Users> findByAccount(String account);

    boolean existsByAccount(String account);
    boolean existsByEmail(String Email);
}