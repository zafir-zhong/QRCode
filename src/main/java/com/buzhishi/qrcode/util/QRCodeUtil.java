package com.buzhishi.qrcode.util;

import com.buzhishi.qrcode.entity.QRCodeFormatEnums;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.UUID;

/**
 * 二维码工具类
 *
 * @author ：zafir zhong
 * @version : 1.0.0
 * @date ：Created in 2021/5/17 下午4:58
 * @modified By：
 */
@Slf4j
public class QRCodeUtil {


    public static void generateQRCodeImageToStream(String text, int size,
                                                   HttpServletResponse response, QRCodeFormatEnums format) throws Exception {
        if (response == null) {
            throw new NullPointerException("导出的流禁止为空");
        }

        if (format == null) {
            format = QRCodeFormatEnums.defaultFormat();
        }
        response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");

        // Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
        ;
        // 方法1： 设置下载的文件的名称-该方式已解决中文乱码问题，swagger,postman看到的是%...等，浏览器直接输url,OK
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(("QRCode"+format.getSuf()).getBytes("UTF-8"),"iso-8859-1"));
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);
        MatrixToImageWriter.writeToStream(bitMatrix, format.getFormat(), response.getOutputStream());
    }

    public static String generateQRCodeImageForBase64(String text, int size, QRCodeFormatEnums format) {
        if (format == null) {
            format = QRCodeFormatEnums.defaultFormat();
        }
        String fileName = UUID.randomUUID().toString() + format.getSuf();
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);
            Path path = FileSystems.getDefault().getPath("images/" + fileName);

            MatrixToImageWriter.writeToPath(bitMatrix, format.getFormat(), path);
            InputStream in = null;
            byte[] data = null;
            // 读取图片字节数组
            in = new FileInputStream("images/" + fileName);
            data = new byte[in.available()];
            in.read(data);
            in.close();
            return "data:image/"+format.getFormat().toLowerCase()+";base64,"+Base64.encodeBase64String(data);
        } catch (Exception e) {
            log.error("读取文件转base64出现错误，错误为：", e);
            return "导出错误";
        } finally {
            File file = new File("images/" + fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static String getMsgForFile(MultipartFile file) throws Exception{
        if (file == null || file.isEmpty() || StringUtils.isEmpty(file.getOriginalFilename())) {
            throw new NullPointerException("图片不存在");
        }
        String originalFilename = file.getOriginalFilename();
        if(originalFilename.lastIndexOf(".") == -1){
            throw new RuntimeException("非法图片格式");
        }
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            File toFile = null;
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File("images/" + fileName);
            toFile.createNewFile();
            OutputStream os = new FileOutputStream("images/"+fileName);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
            os.close();
            MultiFormatReader formatReader = new MultiFormatReader();
            BufferedImage image = ImageIO.read(new File("images/" + fileName));
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            Result result = formatReader.decode(binaryBitmap);
            return result.getText();
        } catch (Exception e) {
            return "读取失败";
        } finally {
            File file2 = new File("images/" + fileName);
            if (file2.exists()) {
                file2.delete();
            }
        }
    }



    public static String getMsgForBase64Str(String text) {
        if (StringUtils.isEmpty(text) ) {
            throw new RuntimeException("非法base64格式");
        }
        if(text.length() <= 22){
            throw new RuntimeException("非法base64格式");
        }
        QRCodeFormatEnums format = QRCodeFormatEnums.getFormat(text.substring(11, 14));
        String fileName = UUID.randomUUID().toString() + format.getSuf();
        try {
            // Base64解码
            byte[] b = Base64.decodeBase64(text.substring(22));
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream("images/" + fileName);
            out.write(b);
            out.flush();
            out.close();
            MultiFormatReader formatReader = new MultiFormatReader();
            BufferedImage image = ImageIO.read(new File("images/" + fileName));
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            Result result = formatReader.decode(binaryBitmap);
            return result.getText();
        } catch (Exception e) {
            log.error("解析二维码失败，错误为：", e);
            return "读取失败";
        } finally {
            File file = new File("images/" + fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
