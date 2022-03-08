package com.shiyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.dto.UserDTO;
import com.shiyi.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统管理-用户基础信息表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-30
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    void insertBackId(User user);

    List<Integer> getMenuId(String userName);

    @Update("update b_user set login_count=login_count+1,last_time=CURRENT_TIMESTAMP where username=#{username}")
    void updateLoginCount(String username);

    Page<UserDTO> selectPageRecord(@Param("page") Page<UserDTO> page, @Param("username")String username, @Param("loginType")Integer loginType);

    User getOne(String username);
}
