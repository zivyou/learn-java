package com.zivyou.learntimerangecrud.service;

import com.zivyou.learntimerangecrud.dao.RunwayTimeRange;
import com.zivyou.learntimerangecrud.mapper.TimeRangeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RunwayTimeRangeService {
    private final TimeRangeMapper timeRangeMapper;


    public RunwayTimeRange getLatestRunwayUsage(String runwayId, String droneId) {
        return timeRangeMapper.queryLatest(runwayId, droneId);
    }

    public List<RunwayTimeRange> listAllRunwayUsage(String runwayId, Date from, Date to) {
        var timeRangeList = timeRangeMapper.query(runwayId, from, to);
        log.info("listAllRunwayUsage by runwayId: {}, {}, {}, results are: {}", runwayId, from, to, timeRangeList);
        return timeRangeList;
    }

    public boolean addRunwayUsage(RunwayTimeRange timeRange) {
        var list = listAllRunwayUsage(timeRange.getRunwayId(), timeRange.getBeginAt(), timeRange.getEndAt());
        if (!list.isEmpty()) {
            return false;
        }
        timeRangeMapper.insert(timeRange);
        return true;
    }

    public void removeRunwayUsage(List<String> ids) {
        timeRangeMapper.delete(ids);
    }
}
