package sni.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sni.common.models.entities.DirectoryAdministratorEntity;
import sni.common.models.entities.DirectoryAdministratorEntityPK;

public interface DirectoryAdministrationsRepository extends JpaRepository<DirectoryAdministratorEntity, DirectoryAdministratorEntityPK>
{
}
