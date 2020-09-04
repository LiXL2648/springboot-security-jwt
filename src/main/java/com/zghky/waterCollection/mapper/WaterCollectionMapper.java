package com.zghky.waterCollection.mapper;

import com.zghky.waterCollection.domain.WaterCollection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WaterCollectionMapper {
    int deleteById(Integer id);

    int insert(WaterCollection waterCollection);

    WaterCollection selectById(Integer id);

    List<WaterCollection> selectAll(@Param("userId") Integer userId, @Param("searchText") String searchText);

    int updateById(WaterCollection waterCollection);

    void isUpdate(Integer id);
}