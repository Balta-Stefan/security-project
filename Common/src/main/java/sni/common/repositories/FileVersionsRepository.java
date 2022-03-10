package sni.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sni.common.models.entities.FileVersionEntity;
import sni.common.models.entities.FileVersionEntityPK;

public interface FileVersionsRepository extends JpaRepository<FileVersionEntity, FileVersionEntityPK>
{
}
