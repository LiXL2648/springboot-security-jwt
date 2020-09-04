package com.li.jwt.service;

import com.github.pagehelper.PageInfo;
import com.li.jwt.domain.WaterCollection;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public interface WaterCollectionService {

    int deleteById(Integer id);

    int insert(WaterCollection waterCollection, HttpServletRequest request);

    WaterCollection selectById(Integer id, HttpServletRequest request);

    PageInfo<WaterCollection> selectAll(Map<String, Object> map, HttpServletRequest request);

    int updateById(WaterCollection waterCollection, HttpServletRequest request);

    String fileUpload(MultipartFile file) throws IOException;

    void deleteFile(String filePath);
}
