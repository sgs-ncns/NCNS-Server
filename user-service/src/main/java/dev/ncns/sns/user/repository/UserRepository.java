package dev.ncns.sns.user.repository;

import dev.ncns.sns.user.domain.AuthType;
import dev.ncns.sns.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByNickname(String nickname);

    Optional<Users> findByAccountName(String accountName);

    boolean existsByAccountName(String accountName);

    boolean existsByEmail(String Email);

    @Query("select u.id from Users u where u.email = :email and u.authType = :authType")
    Long findIdByEmail(String email, AuthType authType);
}
