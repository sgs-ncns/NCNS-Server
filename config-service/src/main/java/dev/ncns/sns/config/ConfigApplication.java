package dev.ncns.sns.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigApplication {

	/**
	 * discovery client 등 모든 서비스에서 중복되는 설정 중앙화, 배포 환경별 DB 설정 관리, 실시간 변경 사항 반영을 위한 Config 서버입니다.
	 * Spring Cloud Config 에서 제공하는 Encrypt/Decrypt 를 통해 원격 저장소에 올라가는 민감 정보를 암호화합니다.
	 * 서버에서 사용되는 암호화 키와 저장소 계정 정보 등은 Jasypt 라이브러리를 이용해 암호화되었습니다.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

}
