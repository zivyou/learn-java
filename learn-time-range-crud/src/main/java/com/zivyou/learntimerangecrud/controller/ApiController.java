package com.zivyou.learntimerangecrud.controller;

import com.zivyou.learntimerangecrud.dao.RunwayTimeRange;
import com.zivyou.learntimerangecrud.service.RunwayTimeRangeService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private final RunwayTimeRangeService runwayTimeRangeService;

    @GetMapping("/runwayTimeRange")
    List<RunwayTimeRange> listAllRunwayTimeRange(@RequestParam String runwayId, @RequestParam String from, @RequestParam String to) {
        if (StringUtils.isBlank(runwayId)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate;
        Date toDate;
        try {
            fromDate = sdf.parse(from);
            toDate = sdf.parse(to);
        } catch (ParseException e) {
            return null;
        }
        return runwayTimeRangeService.listAllRunwayUsage(runwayId, fromDate, toDate);
    }

    @GetMapping("/addRunwayTimeRange")
    boolean addRunwayTimeRange(@RequestParam String runwayId, @RequestParam String from,
                               @RequestParam String to, @RequestParam String droneId) {
        if (StringUtils.isBlank(runwayId)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate;
        Date toDate;
        try {
            fromDate = sdf.parse(from);
            toDate = sdf.parse(to);
        } catch (ParseException e) {
            return false;
        }
        RunwayTimeRange timeRange = new RunwayTimeRange();
        timeRange.setRunwayId(runwayId);
        timeRange.setBeginAt(fromDate);
        timeRange.setEndAt(toDate);
        timeRange.setDroneId(droneId);
        return runwayTimeRangeService.addRunwayUsage(timeRange);
    }

    @GetMapping("/removeRunwayTimeRange")
    boolean removeRunwayTimeRange(@RequestParam String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        runwayTimeRangeService.removeRunwayUsage(idList);
        return true;
    }

    @GetMapping("/getLatestRunwayUsage")
    RunwayTimeRange getLatestRunwayUsage(@RequestParam String runwayId, @RequestParam String droneId) {
        return runwayTimeRangeService.getLatestRunwayUsage(runwayId, droneId);
    }
}
