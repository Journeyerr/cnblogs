package com.hospital.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 待办事项表
 * </p>
 *
 * @author AnYuan
 * @since 2025-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserTodo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建用户ID
     */
    private Long createUserId;

    /**
     * 目标用户ID
     */
    private Long targetUserId;
    
    /**
     * 目标用户
     */
    private String targetUserName;

    /**
     * 待办事项名称
     */
    private String title;

    /**
     * 紧急程度（3-高、2-中、1-低）
     */
    private Integer urgency;

    /**
     * 状态（0-待办、1-进行中、2-已完成）
     */
    private Integer status;

    /**
     * 代办事项内容
     */
    private String content;

    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime executeTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    private Integer deleted;

    /**
     * 操作id集合
     */
    private String operations;


}
