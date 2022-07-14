package com.shiyi.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.dto.CategoryCountDTO;
import com.shiyi.dto.CategoryDTO;
import com.shiyi.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 博客分类表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    List<CategoryCountDTO> countArticle();

    Page<Category> selectPageRecord(@Param("page")Page<Category> objectPage, @Param("name")String name);

    List<CategoryDTO> selectAll();

}
