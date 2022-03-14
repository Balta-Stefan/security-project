package sni.common;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sni.common.models.dtos.DirectoryBasicDTO;
import sni.common.models.dtos.DirectoryDTO;
import sni.common.models.dtos.FileBasicDTO;
import sni.common.models.dtos.FileDTO;
import sni.common.models.entities.FileEntity;

import java.util.List;

@Configuration
public class Config
{
    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<FileEntity, FileDTO> fileDtoMapper = modelMapper.createTypeMap(FileEntity.class, FileDTO.class);
        fileDtoMapper.addMapping(src -> src.getParent().getFileId(), FileDTO::setParent);

        Converter<FileEntity, DirectoryDTO> fileEntityToDirectoryDto = c -> {
            DirectoryDTO rootDir = new DirectoryDTO();
            rootDir.setDirectory(modelMapper.map(c.getSource(), DirectoryBasicDTO.class));

            List<FileBasicDTO> children = c.getSource().getChildren()
                    .stream()
                    .map(f -> modelMapper.map(f, FileBasicDTO.class))
                    .toList();

            rootDir.setChildren(children);

            return rootDir;
        };
        modelMapper.addConverter(fileEntityToDirectoryDto);

        return modelMapper;
    }

}
