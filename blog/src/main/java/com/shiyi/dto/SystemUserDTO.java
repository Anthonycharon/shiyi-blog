package com.shiyi.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author blue
 * @description: 系统用户出参
 * @date 2021/7/30 17:21
 */
@Data
public class SystemUserDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "性别 0:男 1:女")
    private String sex;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "状态")
    private String flag;

    @ApiModelProperty(value = "角色Id")
    private Integer roleId;
}
