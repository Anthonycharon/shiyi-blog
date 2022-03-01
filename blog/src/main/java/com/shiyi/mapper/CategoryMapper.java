package com.shiyi.mapper;

import com.shiyi.dto.CategoryCountDTO;
import com.shiyi.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

}
