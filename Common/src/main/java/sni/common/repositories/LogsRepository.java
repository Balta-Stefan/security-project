package sni.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sni.common.models.entities.FileLogEntity;

public interface LogsRepository extends JpaRepository<FileLogEntity, Long>
{
}
