package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Menu;
import java.util.List;

/**
 * @author blue
 * @description:
 * @date 2021/8/20 9:30
 */
public interface MenuService extends IService<Menu>{

    List<Menu> getMenuTree(List<Menu> list);

    ResponseResult getMenuApi(Integer id);

    ResponseResult saveMenu(Menu menu);

    ResponseResult updateMenu(Menu menu);

    ResponseResult removeMenu(Integer id);
}
