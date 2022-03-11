package sni.common.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import sni.common.exceptions.InternalServerError;
import sni.common.exceptions.NotFoundException;
import sni.common.services.FilePersistenceService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FilePersistenceServiceImpl implements FilePersistenceService
{
    @Value("${file_persistence.path}")
    private String resourcesPath;

    private Path root;

    @PostConstruct
    private void postConstruct()
    {
        this.root = Paths.get(resourcesPath);
        try
        {
            if(Files.exists(root) == false)
            {
                Files.createDirectories(root);
            }
        }
        catch (IOException e)
        {
            log.warn("File service could not initialize upload directory: " , e);
            throw new RuntimeException("Could not initialize file upload directory");
        }
    }

    private String constructFileName(int fileID, short fileVersion)
    {
        return fileID + "v" + fileVersion;
    }

    @Override
    public void persistFile(int fileID, short fileVersion, Resource file) throws IOException
    {
        String fileName = constructFileName(fileID, fileVersion);
        Files.copy(file.getInputStream(), this.root.resolve(fileName));
    }

    @Override
    public Resource getFile(int fileID, short fileVersion)
    {
        String filename = constructFileName(fileID, fileVersion);
        Path file = root.resolve(filename);
        Resource resource;

        try
        {
            resource = new UrlResource(file.toUri());
        }
        catch(Exception e)
        {
            throw new InternalServerError();
        }

        if(resource.exists() || resource.isReadable())
        {
            return resource;
        }
        else
        {
            throw new NotFoundException();
        }
    }
}
