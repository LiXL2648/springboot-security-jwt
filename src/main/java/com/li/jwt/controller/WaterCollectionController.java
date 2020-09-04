package com.li.jwt.controller;

import com.github.pagehelper.PageInfo;
import com.li.jwt.domain.CommonResult;
import com.li.jwt.domain.WaterCollection;
import com.li.jwt.config.MyConstants;
import com.li.jwt.service.WaterCollectionService;
import com.li.jwt.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@RestController
public class WaterCollectionController {

    @Autowired
    private WaterCollectionService waterCollectionService;

    @ModelAttribute
    public void getById(@RequestParam(value = "id", required = false) Integer id,
                        Map<String, Object> map, HttpServletRequest request) {
        if (id != null) {
            WaterCollection waterCollection = waterCollectionService.selectById(id, request);
            map.put("waterCollection", waterCollection);
            request.setAttribute("filePaths", waterCollection.getPhotosPath());
        }
    }

    @PostMapping("/deleteWaterCollectionById")
    public CommonResult<WaterCollection> deleteById(@RequestParam(value = "id", required = true) Integer id) {
        CommonResult<WaterCollection> commonResult = null;
        try {
            waterCollectionService.deleteById(id);
            commonResult = new CommonResult<> (MyConstants.SUCCESS_CODE, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<> (MyConstants.SYSTEM_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @PostMapping("/insertWaterCollection")
    public CommonResult<WaterCollection> insert(WaterCollection waterCollection,
                                                HttpServletRequest request) {
        CommonResult<WaterCollection> commonResult = null;
        try {
            System.out.println(waterCollection);
            waterCollectionService.insert(waterCollection, request);
            commonResult = new CommonResult<> (MyConstants.SUCCESS_CODE, "插入成功");
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<> (MyConstants.SYSTEM_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @PostMapping("/selectWaterCollectionById")
    public CommonResult<WaterCollection> selectById(@RequestParam(value = "id", required = true) Integer id,
                                                    HttpServletRequest request) {
        CommonResult<WaterCollection> commonResult = null;
        try {
            WaterCollection waterCollection = waterCollectionService.selectById(id, request);
            commonResult = new CommonResult<> (MyConstants.SUCCESS_CODE, "查询成功", waterCollection);
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<> (MyConstants.SYSTEM_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @PostMapping("/selectAllWaterCollection")
    public CommonResult<PageInfo<WaterCollection>> selectAll(@RequestParam Map<String, Object> map, HttpServletRequest request) {

        CommonResult<PageInfo<WaterCollection>> commonResult = null;
        try {
            PageInfo<WaterCollection> waterCollections = waterCollectionService.selectAll(map, request);
            commonResult = new CommonResult<> (MyConstants.SUCCESS_CODE, "查询成功", waterCollections);
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<> (MyConstants.SYSTEM_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @PostMapping("/updateWaterCollectionById")
    public CommonResult<WaterCollection> updateById(@ModelAttribute("waterCollection") WaterCollection waterCollection,
                                                    @RequestParam(value = "photosPath", required = false, defaultValue = "") String photosPath,
                                                    HttpServletRequest request) {
        CommonResult<WaterCollection> commonResult = null;
        waterCollection.setPhotosPath(photosPath);
        System.out.println(waterCollection);
        try {
            waterCollectionService.updateById(waterCollection, request);
            commonResult = new CommonResult<> (MyConstants.SUCCESS_CODE, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<> (MyConstants.SYSTEM_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @PostMapping("/fileUpload")
    public CommonResult<String> fileUpload(@RequestParam("file") MultipartFile file) {
        CommonResult<String> commonResult = null;
        try {
            String filePath = waterCollectionService.fileUpload(file);
            if (filePath.isEmpty()) {
                commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, "上传文件为空");
            } else {
                commonResult = new CommonResult<>(MyConstants.SUCCESS_CODE, "上传文件成功", filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            commonResult = new CommonResult<> (MyConstants.SYSTEM_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @GetMapping("/showPicture")
    public void showPicture(@RequestParam(value = "filePath", required = true) String filePath,
                            HttpServletResponse response) {
        FileUtil.printImg(filePath, response);
    }

    @PostMapping("/deleteFile")
    public CommonResult<String> deleteFile(@RequestParam(value = "filePath", required = true) String filePath) {
        CommonResult<String> commonResult = null;
        if (filePath.contains("temp")) {
            commonResult = new CommonResult<>(MyConstants.SUCCESS_CODE, "图片删除成功");
            waterCollectionService.deleteFile(filePath);
        } else {
            commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, "图片不能删除");
        }
        return commonResult;
    }
}
