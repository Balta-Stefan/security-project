package sni.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sni.common.models.entities.FileEntity;

public interface FilesRepository extends JpaRepository<FileEntity, Integer>
{
}
