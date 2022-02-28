package com.ten.utils;

import com.google.common.base.Preconditions;
import com.ten.constant.CommonConstant;
import com.ten.dto.FileDto;
import com.ten.property.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
*/
@Slf4j
public class FileUtil {

    private static final int BUFFER = 1024 * 8;

    private static AppProperties appProperties = SpringContextUtil.getBean(AppProperties.class);
    /**
     * 压缩文件或目录
     *
     * @param fromPath 待压缩文件或路径
     * @param toPath   压缩文件，如 xx.zip
     */
    public static void compress(String fromPath, String toPath) throws IOException {
        File fromFile = new File(fromPath);
        File toFile = new File(toPath);
        if (!fromFile.exists()) {
            throw new FileNotFoundException(fromPath + "不存在！");
        }
        try (
                FileOutputStream outputStream = new FileOutputStream(toFile);
                CheckedOutputStream checkedOutputStream = new CheckedOutputStream(outputStream, new CRC32());
                ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream)
        ){
            String baseDir = "";
            compress(fromFile, zipOutputStream, baseDir);
        }
    }

    /**
     * 压缩文件或目录
     *
     * @param fromPath 待压缩文件或路径
     * @param toPath   压缩文件，如 xx.zip
     */
    public static void compreList(List<String> fromPath, String toPath) {
        File toFile = new File(toPath);
        ZipOutputStream out = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(toFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());
            out = new ZipOutputStream(cos);
            String basedir = "";
            for (String filePath : fromPath) {
                compress(new File(filePath), out, basedir);
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传文件
        * @param fileUpload : 上传的文件
     * @param save :  是否保存
     * @return com.harpy.dto.FileDto
     * @date 2019/10/16 9:42
     */
    public static FileDto upload(MultipartFile fileUpload, boolean save){
        //获取文件名
        String fileName = fileUpload.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")+1);
        //指定本地文件夹存储图片
        String filePath = appProperties.getFileUploadPath();
        String suffixType = fileTypeIs(suffixName);
        filePath=filePath+suffixType+"/";
        mkdirs(filePath);
        String newName = System.currentTimeMillis()+"."+suffixName;
        filePath=filePath+newName;
        try {
            fileUpload.transferTo(new File(filePath));
        } catch (Exception e) {
            log.error("上传文件失败,上传文件类型:{},名字:{}",suffixName,fileName);
            e.printStackTrace();
        }
        FileDto fileDto = new FileDto();
        fileDto.setOldName(fileName);
        fileDto.setNewName(newName);
        fileDto.setSuffixType(suffixType);
        filePath = filePath.replaceAll("\\\\", "/");
        fileDto.setLocalPath(filePath);
        fileDto.setPath("/"+suffixType+"/"+newName);
        if (!save){
            delete(filePath);
        }
        return fileDto;
    }

    /**
     * 文件下载
     *
     * @param filePath 待下载文件路径
     * @param fileName 下载文件名称
     * @param delete   下载后是否删除源文件
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    public static void download(String filePath, String fileName, Boolean delete, HttpServletResponse response) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("文件未找到");
        }
        String fileType = getFileType(file);
        if (!fileTypeIsValid(fileType)) {
            throw new Exception("暂不支持该类型文件下载");
        }
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "utf-8"));
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        try (InputStream inputStream = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } finally {
            if (delete) {
                delete(filePath);
            }
        }
    }


    /**
     * byte数组转图片
        * @date 2021/04/12 15:03
     * @param data  数据
     * @param fileName  文件名字
     * @param filePath  文件路径
     * @return void
     */
    public static String byte2image(byte[] data,String fileName,String filePath){
        filePath = appProperties.getFileUploadPath()+filePath+"/";
        mkdirs(filePath);
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(filePath+fileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return filePath+fileName;
    }
    /**
     * 递归删除文件或目录
     *
     * @param filePath 文件或目录
     */
    public static void delete(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {Arrays.stream(files).forEach(f -> delete(f.getPath()));}
        }
        file.delete();
    }

    /**
     * 获取文件类型
     *
     * @param file 文件
     * @return 文件类型
     * @throws Exception Exception
     */
    public static String getFileType(File file) throws Exception {
        Preconditions.checkNotNull(file);
        if (file.isDirectory()) {
            throw new Exception("file不是文件");
        }
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件类型
     *
     * @param file 文件
     * @return 文件类型
     * @throws Exception Exception
     */
    public static String getFileType(MultipartFile file){
        Preconditions.checkNotNull(file);
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    /**
     * 校验文件类型是否是允许下载的类型
     * （出于安全考虑：https://github.com/wuyouzhuguli/FEBS-Shiro/issues/40）
     *
     * @param fileType fileType
     * @return Boolean
     */
    public static Boolean fileTypeIsValid(String fileType) {
        return fileTypeIsValid(fileType, CommonConstant.VALID_FILE_TYPE);
    }

    /**
     * 校验文件类型是否是该类类型
     * （出于安全考虑：https://github.com/wuyouzhuguli/FEBS-Shiro/issues/40）
     *
     * @param fileType fileType
     * @return Boolean
     */
    public static Boolean fileTypeIsValid(String fileType,String[] valid) {
        Preconditions.checkNotNull(fileType);
        fileType = StringUtils.lowerCase(fileType);
        return ArrayUtils.contains(valid, fileType);
    }



    /**
     * 创建目录，文件夹
        * @param destPath :
     * @return void
     * @date 2019/10/16 9:32
     */
    public static void mkdirs(String destPath) {
        File file =new File(destPath);
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 检验上传文件是什么类型的文件
     *
     * @param fileType fileType
     * @return Boolean
     */
    private static String fileTypeIs(String fileType) {
        Preconditions.checkNotNull(fileType);
        fileType = StringUtils.lowerCase(fileType);
        if(ArrayUtils.contains(CommonConstant.IMAGES_FILE_TYPE, fileType)){
            return "images";
        }else if(ArrayUtils.contains(CommonConstant.VIDEO_FILE_TYPE, fileType)){
            return "video";
        }else if(ArrayUtils.contains(CommonConstant.MUSIC_FILE_TYPE, fileType)){
            return "music";
        }else if("pdf".equals(fileType)){
            return "pdf";
        }else if("qr".equals(fileType)){
            //二维码路径
            return "qr";
        }
        return "file";
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = dir.listFiles();
        if (files != null && ArrayUtils.isNotEmpty(files)) {
            for (File file : files) {
                compress(file, zipOut, baseDir + dir.getName() + "/");
            }
        }
    }

    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (!file.exists()) {
            return;
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }
        }
    }
}

