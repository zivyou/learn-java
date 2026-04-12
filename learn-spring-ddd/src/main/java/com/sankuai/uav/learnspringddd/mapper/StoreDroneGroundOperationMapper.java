package com.sankuai.uav.learnspringddd.mapper;

import com.sankuai.uav.learnspringddd.groundoperation.StoreDroneGroundOperation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StoreDroneGroundOperationMapper {
    @Insert("")
    Integer insert(StoreDroneGroundOperation groundOperation);

    @Select("")
    StoreDroneGroundOperation selectByGroundOperationId(String groundOperationId);
}
