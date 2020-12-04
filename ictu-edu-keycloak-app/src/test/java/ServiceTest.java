import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.globits.config.DatabaseConfig;
import com.globits.security.service.UserService;
//
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseConfig.class)
@Transactional(propagation = Propagation.NEVER)
public class ServiceTest {

	@Autowired
	UserService service;
//	@Autowired
//	PositionRepository positionRepository;
//	
//	@Autowired
//	StaffRepository staffRepository;
	
	@Autowired
	EntityManager entityManager;

	@Test
	public void testGetUserWorks() {
//		Page<UserDto> page = service.findByPage(1, 10);
//		assertTrue(page.getTotalElements() >= 0);
		//positionRepository.getOne(1L);
		
//		Staff staff = new Staff();
//		staff.setFirstName("Test");
//		staff.setStaffCode("0002");
//		staffRepository.save(staff);
//		EventDto dto = new EventDto();
//		dto.setTitle("Test1");
//		dto.setStartTime(DateTime.parse("2020-05-06"));
//		dto.setEndTime(DateTime.parse("2020-05-07"));
//		//eventService.saveOne(dto);
//		
//		Event v = new Event("Test Title");
//		eventService.save(v);
	}
}
