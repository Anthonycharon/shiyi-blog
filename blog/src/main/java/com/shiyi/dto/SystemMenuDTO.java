package com.shiyi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author blue
 * @description: 系统菜单出参
 * @date 2021/7/30 17:21
 */
@Data
public class SystemMenuDTO {

    @ApiModelProperty (value = "id")
    private Integer id;

    @ApiModelProperty(value = "上级资源ID")
    private String parentId;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "资源编码")
    private String resources;

    @ApiModelProperty(value = "资源名称")
    private String title;

    @ApiModelProperty(value = "资源级别")
    private Integer level;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "资源图标")
    private String icon;

    @ApiModelProperty(value = "类型 menu、button")
    private String type;

    @ApiModelProperty(value = "是否拥有此菜单权限，0 未拥有 1拥有")
    private Integer isHas;
}
