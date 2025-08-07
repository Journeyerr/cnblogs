package com.hospital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class UserTodoReqDTO implements Serializable {

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
     * 代办事项内容
     */
    private String content;

    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime executeTime;

    /**
     * 操作id集合
     */
    private String operations;

}
