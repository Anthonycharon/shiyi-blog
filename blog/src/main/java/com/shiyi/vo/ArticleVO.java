package com.shiyi.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.shiyi.utils.DateUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author blue
 * @date 2022/1/25
 * @apiNote
 */
@Data
public class ArticleVO {
    private Long id;
    private Long userId;
    private String title;
    private String avatar;
    private String summary;
    private Integer quantity;
    private String content;
    private String contentMd;
    private Integer isSecret;
    private Integer isStick;
    private Integer isOriginal;
    private String originalUrl;
    private String remark;
    private String keywords;
    private String categoryName;
    private Integer isPublish;
    private List<String> tags;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;
}
