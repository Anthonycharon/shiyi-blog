package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Menu;
import java.util.List;

/**
 * @author blue
 * @description:
 * @date 2021/8/20 9:30
 */
public interface MenuService extends IService<Menu>{

    List<Menu> getMenuTree(List<Menu> list);

    ApiResult getMenuApi(Integer id);

    ApiResult saveMenu(Menu menu);

    ApiResult updateMenu(Menu menu);

    ApiResult removeMenu(Integer id);
}
