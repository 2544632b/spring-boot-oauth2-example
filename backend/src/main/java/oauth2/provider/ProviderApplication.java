package oauth2.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 *  Platform: Windows 11 22H2 22621.1413
 *  Java version: 17.0.7 2023-04-18 LTS
 *  Java(TM) SE Runtime Environment Oracle GraalVM: build 17.0.7+8-LTS-jvmci-23.0-b12
 *  Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM: build 17.0.7+8-LTS-jvmci-23.0-b12, mixed mode, sharing
 */


@MapperScan(basePackages = "oauth2.provider.v2.user.mapper")

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
public class ProviderApplication {
	public static void main(String[] args) {
		System.out.println("Staring spring boot, please wait...");
		SpringApplication.run(ProviderApplication.class, args);
	}
}
