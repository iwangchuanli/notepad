package com.ten.constant;

/**
 * 公共常量
 */
public class CommonConstant {

    /**
     * 排序规则：降序
     */
    public static final String ORDER_DESC = "desc";

    /**
     * 排序规则：升序
     */
    public static final String ORDER_ASC = "asc";

    public static final String PASSWORD_KEY ="YUIwIWFEMSFlQzAqckUwW2VBMCRhQzYqcUowe2VN";

    /** token redis Key */
    public static final String TOKEN_REDIS_KEY="notepad_token:";

    public static final String EMAIL_CODE_REDIS_KEY="notepad_email_code_key:";

    /** 图片文件类型（小写） */
    public static final String[] IMAGES_FILE_TYPE={"jpg","png","jpeg","gif","bmp","jfif","pjpeg","pjp"};

    /** 视频文件类型（小写） */
    public static final String[] VIDEO_FILE_TYPE={"wmv","avi","dat","asf","rm","rmvb","ram","mpg","mpeg",
            "3gp","mov","mp4","m4v","flv","qt","ram"};

    /** 音频文件类型（小写） */
    public static final String[] MUSIC_FILE_TYPE={"au","snd","mid","asf","rmi","mp3","aif","aifc","aiff",
            "m3u","ra","ram","wav"};

    /**允许下载的文件类型，根据需求自己添加（小写） */
    public static final String[] VALID_FILE_TYPE = {"xlsx", "zip","jpg","png","jpeg","pdf"};

    /** 配置文件redis前缀  */
    public static final String SYSTEM_CONFIG_KEY="notepad_sys_config";

}
