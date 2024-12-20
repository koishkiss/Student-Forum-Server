package student.forum;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;
import student.forum.model.CONSTANT.MAPPER;
import student.forum.model.bo.SinglePageSearchBO;
import student.forum.model.po.User;
import student.forum.model.vo.Response;
import student.forum.model.vo.SinglePageVO;
import student.forum.service.DataService;
import student.forum.util.TextUtil;

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

	@Test
	void testTran() {
//		System.out.println(TextUtil.truncatedLinesAndWords(5,50,"为深入学习贯彻党的二十大精神，落实习近平总书记对青年志愿服务工作的重要论述，在第39个“12·5”国际志愿者日到来之际，12月5日和7日，山东大学2024年度济南校本部志愿服务先进典型终审答辩会暨“志青春”分享会和山东大学青年志愿者联合会第十八次理事会在中心校区举行。"));
	}

	@Test
	void testReplyGet() {
		DataService dataService = new DataService();
		Response response = dataService.getMyMessage(2,new SinglePageSearchBO<>());
		System.out.println(response.getData());
//		System.out.println(MAPPER.reply.getReplyReplyByUidOnFirstTime(2, 20));
	}

}
