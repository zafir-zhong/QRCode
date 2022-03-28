package com.buzhishi.qrcode.controller;

import com.buzhishi.qrcode.entity.QRCodeFormatEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.buzhishi.qrcode.util.QRCodeUtil;

import javax.servlet.http.HttpServletResponse;


@RestController
@Api(tags = "QRCode message to images")
@RequestMapping("toImage")
public class QRCodeToImagesController {

    @GetMapping("/base64")
    @ApiOperation("传入")
    public String base64(
            @RequestParam(required = true) String text,
            @RequestParam(required = false, defaultValue = "100") int size,
            @RequestParam(required = false, defaultValue = "JPG") String format) {
        return QRCodeUtil.generateQRCodeImageForBase64(text, size, QRCodeFormatEnums.getFormat(format));
    }

    @GetMapping(value = "/image",produces = "application/octet-stream")
    @ApiOperation("变量获取，必须传入类型")
    public String image(
            @RequestParam(required = true) String text,
            @RequestParam(required = false, defaultValue = "100") int size,
            @RequestParam(required = false, defaultValue = "JPG") String format,
            HttpServletResponse response) throws Exception{
        QRCodeUtil.generateQRCodeImageToStream(text, size, response,QRCodeFormatEnums.getFormat(format));
        return null;
    }
}