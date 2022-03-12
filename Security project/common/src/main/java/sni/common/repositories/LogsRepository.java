package sni.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sni.common.models.entities.FileEntity;
import sni.common.models.entities.FileLogEntity;

import java.util.List;

public interface LogsRepository extends JpaRepository<FileLogEntity, Long>
{
    List<FileLogEntity> findAllByAffectedFile(FileEntity affectedFile);
}
