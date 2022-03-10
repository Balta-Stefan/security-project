package sni.common;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sni.common.models.dtos.FileDTO;
import sni.common.models.entities.FileEntity;

@Configuration
public class Config
{
    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<FileEntity, FileDTO> fileDtoMapper = modelMapper.createTypeMap(FileEntity.class, FileDTO.class);
        fileDtoMapper.addMapping(src -> src.getParent().getFileId(), FileDTO::setParent);

        return modelMapper;
    }

}
