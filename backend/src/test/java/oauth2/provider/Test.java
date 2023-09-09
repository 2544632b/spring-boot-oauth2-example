package oauth2.provider;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
class Test {

	@Resource
	private WebApplicationContext wac;
	private MockMvc mmvc;

	@BeforeEach
	void init() {
		mmvc = MockMvcBuilders.webAppContextSetup(wac).alwaysDo(print()).build();
	}

	@SneakyThrows
	@org.junit.jupiter.api.Test
	void contextLoads() throws Exception {
	}
}
