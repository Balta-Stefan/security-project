package sni.common.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sni.common.models.entities.UserEntity;
import sni.common.models.enums.Role;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserEntity, Integer>
{
    Optional<UserEntity> findByOidcIssAndOidcSub(String OidcIss, String OidcSub);

    @Query("SELECT u FROM UserEntity u WHERE" +
            "(:containsInUserName IS NULL OR u.username LIKE %:containsInUserName%) AND" +
            "(:role IS NULL OR u.role=:role)")
    List<UserEntity> filterUsers(String containsInUserName, Role role, Pageable pageable);

    List<UserEntity> findAllByRole(Role role);
}
