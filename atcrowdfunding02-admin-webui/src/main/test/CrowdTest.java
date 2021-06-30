import com.atroot.crowd.entity.Admin;
import com.atroot.crowd.mapper.AdminMapper;
import com.atroot.crowd.service.api.AdminService;
import com.atroot.crowd.service.api.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Description:测试类
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.5.19 17:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml", "classpath:spring-persist-tx.xml"})
public class CrowdTest {
    @Autowired
    DataSource dataSource;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    AdminService adminService;

    @Autowired
    RoleService roleService;

    @Test
    public void testConnect() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testInsert() {
        Admin admin = new Admin(null, "tom", "123456", "汤姆猫", "tom@qq.com", null);
        int insert = adminMapper.insert(admin);
        System.out.println("影响行数：" + insert);
    }
// 给数据库中插入数据
//    @Test
//    public void testForSave(){
//        Admin admin = null;
//        for (int i = 0; i < 99; i++) {
//
//            admin = new Admin(null, "Jerry" + i, "123456", "杰瑞" + i, "jerry@126.com", null);
//            adminService.saveAdmin(admin);
//        }
//
//    }
//
//    @Test
//    public void testForSave2(){
//        Role role = null;
//        for (int i = 0; i < 99; i++) {
//
//            role = new Role(null, "role" + i);
//            roleService.save(role);
//        }
//    }


}
