package com.li.jwt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.li.jwt.config.jwt.JwtTokenUtil;
import com.li.jwt.domain.WaterCollection;
import com.li.jwt.mapper.WaterCollectionMapper;
import com.li.jwt.service.WaterCollectionService;
import com.li.jwt.config.MyConstants;
import com.li.jwt.util.FileUtil;
import com.li.jwt.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service("WaterCollectionService")
public class WaterCollectionServiceImpl implements WaterCollectionService {

    @Autowired
    private WaterCollectionMapper waterCollectionMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public int deleteById(Integer id) {
        return waterCollectionMapper.deleteById(id);
    }

    @Override
    @Transactional
    public int insert(WaterCollection waterCollection, HttpServletRequest request) {
        List<String> newFilePathList = new ArrayList<>();

        List<String> filePathList = null;
        if(waterCollection.getPhotosPath() != null && !waterCollection.getPhotosPath().isEmpty()) {
            Arrays.asList(waterCollection.getPhotosPath().split(";"));
        }
        try {
            Timestamp date = new Timestamp(System.currentTimeMillis());
            if (filePathList != null && filePathList.size() > 0) {
                for (String filePath : filePathList) {
                    String newFilePath = FileUtil.removefile(filePath, FileUtil.getFileDir(date));
                    newFilePathList.add(newFilePath);
                }
                waterCollection.setPhotosPath(String.join(";", newFilePathList));
            }
            int userId = ObjectUtil.getUserId(request, jwtTokenUtil);
            waterCollection.setWaterNum("12345678");
            waterCollection.setCreateUserId(userId);
            waterCollection.setCreateTime(date);
            waterCollection.setUpdateUserId(userId);
            waterCollection.setUpdateTime(date);
            return waterCollectionMapper.insert(waterCollection);
        } catch (Exception e) {
            if (newFilePathList.size() > 0) {
                for (String newFilePath : newFilePathList) {
                    FileUtil.removefile(newFilePath, MyConstants.TEMP_PATH);
                }
            }
            throw e;
        }
    }

    @Override
    @Transactional
    public WaterCollection selectById(Integer id, HttpServletRequest request) {
        int userId = ObjectUtil.getUserId(request, jwtTokenUtil);
        WaterCollection waterCollection = waterCollectionMapper.selectById(id);
        if (waterCollection.getUpdateUserId() == 1 && userId != waterCollection.getUpdateUserId()) {
            waterCollectionMapper.isUpdate(waterCollection.getId());
        }
        return waterCollection;
    }

    @Override
    public PageInfo<WaterCollection> selectAll(Map<String, Object> map, HttpServletRequest request) {
        Integer userId = ObjectUtil.getUserId(request, jwtTokenUtil);
        userId = userId == 1 ? null : userId;
        String searchText = (String) map.get("searchText");
        if (map.get("pageNum") != null && map.get("pageSize") != null) {
            int pageNum = Integer.parseInt((String) map.get("pageNum"));
            int pageSize = Integer.parseInt((String) map.get("pageSize"));
            PageHelper.startPage(pageNum, pageSize);
        }
        List<WaterCollection> waterCollections = waterCollectionMapper.selectAll(userId, searchText);
        PageInfo<WaterCollection> pageInfo = new PageInfo<>(waterCollections);

        return pageInfo;
    }

    @Override
    @Transactional
    public int updateById(WaterCollection waterCollection, HttpServletRequest request) {
        String filePaths = (String) request.getAttribute("filePaths");
        List<String> oldFilePathList = null, tempFilePathList = null, deleteFilePathList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        try {
            Timestamp date = new Timestamp(System.currentTimeMillis());
            if (waterCollection.getPhotosPath() == null || waterCollection.getPhotosPath().isEmpty()) {
                if (filePaths != null && !filePaths.isEmpty()) {
                    // 删除图片
                    oldFilePathList = Arrays.asList(filePaths.split(";"));
                    for (String oldFilePath: oldFilePathList) {
                        String tempFilePath = FileUtil.removefile(oldFilePath, MyConstants.TEMP_PATH);
                        map.put(tempFilePath, oldFilePath);
                        deleteFilePathList.add(tempFilePath);
                    }
                }
            } else {
                if (filePaths == null || filePaths.isEmpty()) {
                    // 新增图片
                    tempFilePathList = Arrays.asList(waterCollection.getPhotosPath().split(";"));
                    for (String tempFilePath : tempFilePathList) {
                        String newFilePath = FileUtil.removefile(tempFilePath, FileUtil.getFileDir(date));
                        map.put(newFilePath, tempFilePath);
                    }
                } else {
                    // 替换
                    oldFilePathList = Arrays.asList(filePaths.split(";"));
                    tempFilePathList = Arrays.asList(waterCollection.getPhotosPath().split(";"));
                    for (String oldFilePath: oldFilePathList) {
                        if (!tempFilePathList.contains(oldFilePath)) {
                            // 删除图片
                            String tempFilePath = FileUtil.removefile(oldFilePath, MyConstants.TEMP_PATH);
                            map.put(tempFilePath, oldFilePath);
                            deleteFilePathList.add(tempFilePath);
                        }
                    }

                    for (String tempFilePath : tempFilePathList) {
                        if (!oldFilePathList.contains(tempFilePath)) {
                            // 新增图片
                            String newFilePath = FileUtil.removefile(tempFilePath, FileUtil.getFileDir(date));
                            map.put(newFilePath, tempFilePath);
                        }
                    }
                }
            }

            waterCollection.setPhotosPath(waterCollection.getPhotosPath().replaceAll("temp", ObjectUtil.getYearMonth(date)));
            int userId = ObjectUtil.getUserId(request, jwtTokenUtil);
            if (userId != waterCollection.getCreateUserId()) {
                waterCollection.setUpdateUserId(userId);
                waterCollection.setIsUpdate(true);
            }
            waterCollection.setUpdateTime(date);
            return waterCollectionMapper.updateById(waterCollection);
        } catch (Exception e) {
            if (!map.isEmpty()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    File startFile = new File(entry.getKey());
                    File endFile = new File(entry.getValue());
                    startFile.renameTo(endFile);
                }
            }
            throw e;
        } finally {
            if (deleteFilePathList.size() > 0) {
                for (String deleteFilePath: deleteFilePathList) {
                    FileUtil.deleteFile(deleteFilePath);
                }
            }
        }
    }

    public String fileUpload(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Date date = new Date();
            int rand = (int) ((Math.random() * 9 + 1) * 10000);
            String[] fileNames = file.getOriginalFilename().split("\\.");
            String filePath = MyConstants.TEMP_PATH + "\\" + date.getTime() + rand + "." + fileNames[fileNames.length - 1];
            file.transferTo(new File(filePath));
            return filePath;
        }
        return null;
    }

    public void deleteFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
