package com.server.bitwit.module.file.mapper;

import com.server.bitwit.domain.UploadFile;
import com.server.bitwit.infra.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public interface UploadFileUrlMapper extends Converter<UploadFile, String> {
    
    @Override
    default String convert(UploadFile uploadFile) {
        if (uploadFile == null) {
            return null;
        }
        return "http://bitwit.site/images/" + uploadFile.getUploadFileName( );
    }
}
