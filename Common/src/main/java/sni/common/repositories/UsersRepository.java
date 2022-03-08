package sni.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sni.common.models.entities.UserEntity;

public interface UsersRepository extends JpaRepository<UserEntity, Integer>
{
}
