package oauth2.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
