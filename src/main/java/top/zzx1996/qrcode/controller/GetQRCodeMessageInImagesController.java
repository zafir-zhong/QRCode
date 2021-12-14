package top.zzx1996.qrcode.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zzx1996.qrcode.entity.GetMessageRequest;
import top.zzx1996.qrcode.util.QRCodeUtil;


@RestController
@Api(tags = "Get QRCode message in images ")
@Slf4j
@RequestMapping("toMessage")
public class GetQRCodeMessageInImagesController {


    @PostMapping("/messageInFile")
    @ApiOperation("解析")
    @ApiImplicitParam(name = "file", value = "二维码图片", required = true, paramType = "query")
    public String getMsgInFile(
            @RequestParam("file") MultipartFile[] file) throws Exception {
        if (file == null || file[0] == null) {
            throw new RuntimeException("禁止提交空数据");
        }
        return QRCodeUtil.getMsgForFile(file[0]);
    }


    @PostMapping("/messageInBase64")
    @ApiOperation("解析base64")
    public String getMsgInBase64(
            @RequestBody String image) throws Exception {
        if (StringUtils.isEmpty(image)) {
            throw new RuntimeException("禁止提交空数据");
        }
        if(image.startsWith("image=")){
            image = image.substring(6);
        }
        return QRCodeUtil.getMsgForBase64Str(image);
    }

}
