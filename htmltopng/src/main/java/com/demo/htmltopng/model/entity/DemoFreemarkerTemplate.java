package com.demo.htmltopng.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author AnYuan
 * @since 2021-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DemoFreemarkerTemplate implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String code;

    private String value;


}
