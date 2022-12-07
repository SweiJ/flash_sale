package cn.swei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by swei
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "cn.swei.mapper")
public class UaaApplication {
    public static void main(String[] args) {
        SpringApplication.run(UaaApplication.class,args);
    }
}
