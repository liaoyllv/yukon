package com.lyl.yukon.common.constant;

/**
 * <p>资料常量</p>
 *
 * @author liaoyl
 * @version 1.0 2018/11/14 15:09
 **/
public class MaterialConstant {

    private MaterialConstant() {
    }

    /**
     * 资料类型：0-视频，1-音频，2-文档，3-资料包，4-图片
     */
    public static final byte MATERIAL_TYPE_VIDEO = 0;
    public static final byte MATERIAL_TYPE_AUDIO = 1;
    public static final byte MATERIAL_TYPE_DOC = 2;
    public static final byte MATERIAL_TYPE_ZIP = 3;
    public static final byte MATERIAL_TYPE_PICTURE = 4;
    /**
     * 关联类型：0-课程资料
     */
    public static final byte RELATION_COURSE = 0;
    /**
     * 图片的后缀
     */
    public static final String MATERIAL_PIC_SUFFIX = ".png";

}
