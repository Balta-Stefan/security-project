package sni.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sni.common.models.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserEntity, Integer>
{
    Optional<UserEntity> findByOidcIssAndOidcSub(String OidcIss, String OidcSub);
}
