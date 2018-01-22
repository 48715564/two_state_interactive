package com.cn.common.persistence;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by bozhou on 2017/11/20.
 */
public interface CrudWithBLOBsMapper<Example,Entity> extends CrudMapper<Example,Entity>{
    List<Entity> selectByExampleWithBLOBs(Example example);
    int updateByExampleWithBLOBs(@Param("record") Entity record, @Param("example") Example example);
    int updateByPrimaryKeyWithBLOBs(Entity record);
}
