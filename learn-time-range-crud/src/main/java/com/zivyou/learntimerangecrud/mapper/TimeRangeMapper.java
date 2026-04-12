package com.zivyou.learntimerangecrud.mapper;

import com.zivyou.learntimerangecrud.dao.RunwayTimeRange;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface TimeRangeMapper {
    @Insert("insert into `runway_time_range_schedule` (runway_id, begin_at, end_at, drone_id) values (#{runwayId}, #{beginAt}, #{endAt}, #{droneId})")
    void insert(RunwayTimeRange runwayTimeRange);
    @Select("select * from runway_time_range_schedule where runway_id=#{runwayId} and (\n" +
            "    ((#{from}>=begin_at and #{from}<=end_at) or (#{to}>=begin_at and #{to}<=end_at))\n" +
            "    or\n" +
            "    ((begin_at>=#{from} and begin_at<=#{to}) or (end_at>=#{from} and end_at<=#{to}))\n" +
            "    );")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "runwayId", column = "runway_id"),
            @Result(property = "beginAt", column = "begin_at"),
            @Result(property = "endAt", column = "end_at"),
            @Result(property = "droneId", column = "drone_id")
    })
    List<RunwayTimeRange> query(String runwayId, Date from, Date to);

//    boolean update(RunwayTimeRange runwayTimeRange);
    @Delete("delete * from `runway_time_range_schedule` where runway_id=#{runwayId} in #{runwayIds}")
    boolean delete(List<String> runwayIds);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "runwayId", column = "runway_id"),
            @Result(property = "beginAt", column = "begin_at"),
            @Result(property = "endAt", column = "end_at"),
            @Result(property = "droneId", column = "drone_id")
    })
    @Select("select * from `runway_time_range_schedule` where runway_id=#{runwayId} and drone_id=#{droneId} order by end_at desc limit 1")
    RunwayTimeRange queryLatest(String runwayId, String droneId);
}
