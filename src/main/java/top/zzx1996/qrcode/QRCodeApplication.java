package top.zzx1996.qrcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author cuixiuyin
 * @description
 * @date: 2018/12/19 21:32
 */
@SpringBootApplication
@EnableSwagger2WebMvc
public class QRCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(QRCodeApplication.class, args);
    }
}
