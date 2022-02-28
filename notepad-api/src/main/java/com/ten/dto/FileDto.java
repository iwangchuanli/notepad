package com.ten.dto;

import lombok.Data;

/**
* @date 2019/10/16 9:16
 */
@Data
public class FileDto {

    /**
     * 旧名字
     */
    private String oldName;

    /**
     * 新名字
     */
    private String newName;

    /**
     *  地址
     */
    private String path;

    /**
     * 物理地址
     */
    private String localPath;
    /**
     * 后缀类型
     */
    private String suffixType;

}
