package com.ten.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ten.constant.CommonConstant;
import com.ten.dto.FileDto;
import com.ten.enums.ResultEnum;
import com.ten.exception.SysRunException;
import com.ten.utils.FileUtil;
import com.ten.vo.BaseResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 *  文件上传
* @date 2021/6/10 15:17
 */
@RestController
@RequestMapping("/upload")
public class FileUploadController {


    /**
     * 上传文件
        * @date 2021/6/10 15:57
     * @param image
     * @return com.bt.common.vo.BaseResVO
     */
    @PostMapping("/image")
    public BaseResultVO upload(@RequestParam("image") MultipartFile image){
        if(image.isEmpty()){
            return BaseResultVO.fail(ResultEnum.UPLOAD_NOT_EXISTENT);
        }
        FileDto upload = this.uploadUtil(image);
        Map<String,String> result = Maps.newHashMap();
        result.put("path",upload.getPath());
        result.put("name",upload.getOldName());
        return BaseResultVO.success(result);
    }

    /**
     * 上传文件
        * @date 2021/6/10 15:57
     * @param imageArr
     * @return com.bt.common.vo.BaseResVO
     */
    @PostMapping("/imageArr")
    public BaseResultVO upload(@RequestParam("files") MultipartFile[] imageArr){
        if(imageArr.length<1){
            return BaseResultVO.fail(ResultEnum.UPLOAD_NOT_EXISTENT);
        }
        List<FileDto> fileDtoList = Lists.newArrayList();
        for (MultipartFile image : imageArr) {
            FileDto fileDto = this.uploadUtil(image);
            fileDtoList.add(fileDto);
        }
        List<Map<String,String>> result = Lists.newArrayList();
        fileDtoList.forEach(e->{
            Map<String,String> map = Maps.newHashMap();
            map.put("path",e.getPath());
            map.put("name",e.getOldName());
            result.add(map);
        });
        return BaseResultVO.success(result);
    }

    private FileDto uploadUtil(MultipartFile image){
        String fileType = FileUtil.getFileType(image);
        Boolean bo = FileUtil.fileTypeIsValid(fileType, CommonConstant.IMAGES_FILE_TYPE);
        if(!bo){
            throw new SysRunException(ResultEnum.UPLOAD_FILE_TYPE_ERROR);
        }
        return FileUtil.upload(image, true);
    }
}
