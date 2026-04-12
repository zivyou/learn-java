package com.zivyou.learntimerangecrud.dao;

import lombok.Data;

import java.util.Date;

@Data
public class RunwayTimeRange {
    Long id;
    String runwayId;
    Date beginAt;
    Date endAt;
    String droneId;
}
