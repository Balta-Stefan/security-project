package sni.common;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sni.common.models.dtos.*;
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

        // DirectoryBasicDTO converter
        Converter<FileEntity, DirectoryBasicDTO> fileEntityToDirBasicDTO = c ->
        {
            FileEntity src = c.getSource();
            DirectoryBasicDTO dir = new DirectoryBasicDTO();

            dir.setFileId(src.getFileId());
            dir.setName(src.getName());

            if(src.getParent() != null)
                dir.setParentID(src.getParent().getFileId());

            return dir;
        };
        TypeMap<FileEntity, DirectoryBasicDTO> fileEntToDirectoryBasicTypeMap = modelMapper.createTypeMap(FileEntity.class, DirectoryBasicDTO.class);
        fileEntToDirectoryBasicTypeMap.setConverter(fileEntityToDirBasicDTO);

        // DirectoryDTO converter
        Converter<FileEntity, DirectoryDTO> fileEntityToDirectoryDto = c ->
        {
            FileEntity source = c.getSource();

            DirectoryDTO rootDir = new DirectoryDTO();
            rootDir.setDirectory(modelMapper.map(source, DirectoryBasicDTO.class));

            List<FileBasicDTO> children = source.getChildren()
                    .stream()
                    .map(f -> modelMapper.map(f, FileBasicDTO.class))
                    .toList();
            rootDir.setChildren(children);

            return rootDir;
        };
        TypeMap<FileEntity, DirectoryDTO> fileEntToDirectoryDtoTypeMap = modelMapper.createTypeMap(FileEntity.class, DirectoryDTO.class);
        fileEntToDirectoryDtoTypeMap.setConverter(fileEntityToDirectoryDto);

        return modelMapper;
    }

}
