package com.shiyi.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author blue
 * @date 2022/1/14
 * @apiNote
 */
@Data
public class ArticleRecoDTO {
    private Integer id;
    private String title;
    private String avatar;
    private Date createTime;
}
