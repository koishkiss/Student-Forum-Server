package student.forum;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.po.User;

@SpringBootTest
class StudentForumServerApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	void addTestAccount() {
//		MAPPER.user.register("202000300514", BCrypt.hashpw("123456",BCrypt.gensalt()), "202000300514");
//		MAPPER.user.register("202051400514", BCrypt.hashpw("654321",BCrypt.gensalt()), "202051400514");
	}

}
