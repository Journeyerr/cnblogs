package com.cnblog.seckill.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cnblog.seckill.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AnYuan
 * @since 
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    
    int decrementStock(@Param("sku") String sku, @Param("quantity") int quantity);
}
