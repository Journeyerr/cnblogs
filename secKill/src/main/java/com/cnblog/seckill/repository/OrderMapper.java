package com.cnblog.seckill.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cnblog.seckill.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AnYuan
 * @since
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    Order selectByOrderId(@Param("orderId") String orderId);
}
