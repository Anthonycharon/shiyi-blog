package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.Constants;
import com.shiyi.common.SqlConf;
import com.shiyi.dto.SystemUserDTO;
import com.shiyi.entity.Menu;
import com.shiyi.entity.RoleMenu;
import com.shiyi.entity.UserRole;
import com.shiyi.mapper.MenuMapper;
import com.shiyi.mapper.RoleMenuMapper;
import com.shiyi.mapper.UserRoleMapper;
import com.shiyi.service.MenuService;
import com.shiyi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.dto.SystemMenuDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统管理-权限资源表  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-09-24
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /**
     * 获取菜单树
     * @param list
     * @return
     */
    @Override
    public List<Menu> getMenuTree(List<Menu> list) {
        List<Menu> resultList = new ArrayList<>();
        for (Menu menu : list) {
            Integer parentId = menu.getParentId();
            if ( parentId == null || parentId == 0)
                resultList.add(menu);
        }
        for (Menu menu : resultList) {
            menu.setChildren(getChild(menu.getId(),list));
        }
        return resultList;
    }

    /**
     * 接口列表
     * @return
     */
    @Override
    public ApiResult getMenuApi(Integer id) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>()
                .eq("level",1).eq(id != null, SqlConf.ID,id);
        List<Menu> list = baseMapper.selectList(queryWrapper);
        for (Menu menu : list) {
            List<Menu> childrens = baseMapper.selectList(new QueryWrapper<Menu>().eq(SqlConf.PARENT_ID,menu.getId()));
            menu.setChildren(childrens);
        }
        return ApiResult.success(list);
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult saveMenu(Menu menu) {
        int rows = baseMapper.insert(menu);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("添加菜单失败");
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateMenu(Menu menu) {
        int rows = baseMapper.updateById(menu);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("修改菜单失败");
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult removeMenu(Integer id) {
        int rows = baseMapper.deleteById(id);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("删除菜单失败");
    }


    //----------------自定义方法开始------------
    private List<Menu> getChild(Integer pid , List<Menu> menus){
        List<Menu> childrens = new ArrayList<>();
        for (Menu e: menus) {
            Integer parentId = e.getParentId();
            if(parentId != null && parentId.equals(pid)){
                // 子菜单的下级菜单
                childrens.add( e );
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Menu e: childrens) {
            // childrens
            e.setChildren(getChild(e.getId(),menus));
        }
        //停下来的条件，如果 没有子元素了，则停下来
        if( childrens.size()==0 ){
            return null;
        }
        return childrens;
    }
}
