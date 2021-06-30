package com.atroot.crowd.service.impl;

import com.atroot.crowd.constant.CrowdConstant;
import com.atroot.crowd.entity.Admin;
import com.atroot.crowd.entity.AdminExample;
import com.atroot.crowd.exception.LoginAcctAlreadyInUseException;
import com.atroot.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atroot.crowd.exception.LoginFailedException;
import com.atroot.crowd.mapper.AdminMapper;
import com.atroot.crowd.service.api.AdminService;
import com.atroot.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.5.20 10:58
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    @Override
    public void saveAdmin(Admin admin) {
        // 拿到密码准备进行加密
        String userPswd = admin.getUserPswd();
        String md5 = CrowdUtil.md5(userPswd);
        // 重新设置加密后的密码
        admin.setUserPswd(md5);
        // 添加一个创建时间
        Date date = new Date();
        // 进行格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String creatTime = simpleDateFormat.format(date);
        admin.setCreateTime(creatTime);
        // 保存
        // 抛出异常
        try {
            adminMapper.insert(admin);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 1、根据登录账号查询Admin对象
        // 创建AdminExample对象
        AdminExample adminExample = new AdminExample();

        // 创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();

        // 在Criteria对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);

        // 调用AdminMapper的方法进行查询
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        if (admins == null || admins.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        } else if (admins.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        // 获取到Admin对象
        Admin admin = admins.get(0);
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 2、如果用户存在，则准备进行取数据库密码
        // 拿到admin对象的密码准备和表单提交上来的进行比较(数据库密码)
        String userPswdDb = admin.getUserPswd();
        // 3、进行对表单的密码加密
        String userPswdForm = CrowdUtil.md5(userPswd);
        // 4、比较
        if (Objects.equals(userPswdDb, userPswdForm)) {
            return admin;
        } else {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 开启分页插件的分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 调用adminMapper进行查询
        List<Admin> admins = adminMapper.selectAdminByKeyword(keyword);

        // 将List<admin>封装为PageInfo
        PageInfo<Admin> pageInfo = new PageInfo<>(admins);
        return pageInfo;
    }

    @Override
    public void remove(Integer id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Admin getAdminById(Integer id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        return admin;
    }

    @Override
    public void updateAdmin(Admin admin) {

        // 按主键选择性更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 删除旧的数据
        adminMapper.deleteOldRoleRelationship(adminId);

        // 添加新的数据
        if (roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRoleRelationship(adminId, roleIdList);
        }
    }
}
