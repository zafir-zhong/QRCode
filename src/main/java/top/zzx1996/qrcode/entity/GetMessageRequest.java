package top.zzx1996.qrcode.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 获取二维码内信息的请求
 *
 * @author zafir zhong
 * @version 1.0.0
 * @date created at 2021/12/13 10:16 下午 ; update at
 */
@Data
public class GetMessageRequest {

    private String image;

}
