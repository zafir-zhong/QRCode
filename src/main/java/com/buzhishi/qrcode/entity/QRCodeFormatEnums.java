package com.buzhishi.qrcode.entity;

import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 二维码图片格式枚举
 *
 * @author ：zafir zhong
 * @version : 1.0.0
 * @date ：Created in 2021/5/17 下午5:02
 * @modified By：
 */
public enum QRCodeFormatEnums {

    PNG("PNG",".png"),
    JPG("JPG",".jpg"),
    ;

    private String format;
    private String suf;

    QRCodeFormatEnums(String format, String suf) {
        this.format = format;
        this.suf = suf;
    }
    public static QRCodeFormatEnums getFormat(String format){
        if(StringUtils.isEmpty(format)){
            return defaultFormat();
        }
        for (QRCodeFormatEnums value : QRCodeFormatEnums.values()) {
            if(Objects.equals(format.toUpperCase(), value.getFormat())){
                return value;
            }
        }
        return defaultFormat();
    }

    public static QRCodeFormatEnums defaultFormat(){
        return QRCodeFormatEnums.PNG;
    }

    public String getFormat() {
        return format;
    }

    public String getSuf() {
        return suf;
    }
}
