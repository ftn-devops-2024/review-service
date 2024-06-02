package com.devops.reviewservice;

import com.devops.reviewservice.controller.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

@WebMvcTest(TestController.class)
class ReviewserviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
