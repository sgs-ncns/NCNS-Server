package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByAccountName(String accountName);

    boolean existsById(Long id);

    boolean existsByEmail(String Email);

    boolean existsByAccountName(String accountName);
}
