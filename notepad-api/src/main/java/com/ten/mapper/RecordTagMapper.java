package com.ten.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.ten.entity.RecordTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 记事本标签关联表 Mapper
 *
* @date 2021-11-19 10:48:32
 */
@Mapper
public interface RecordTagMapper extends BaseMapper<RecordTag> {

}
