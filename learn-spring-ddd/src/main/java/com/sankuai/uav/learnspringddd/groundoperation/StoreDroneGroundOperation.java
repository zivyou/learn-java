package com.sankuai.uav.learnspringddd.groundoperation;


import com.sankuai.uav.learnspringddd.mapper.StoreDroneGroundOperationMapper;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

@Data
@Builder
public class StoreDroneGroundOperation implements GroundOperation, ApplicationContextAware {

    private String droneId;
    private String runwayId;

    @Override
    public void start() {}

    @Override
    public void block(Long time) {}

    @Override
    public void failed(Integer reason) {}

    @Override
    public void complete() {}

    public static StoreDroneGroundOperation of(String groundOperationId) {
        if (applicationContext == null) return null;
        StoreDroneGroundOperationMapper mapper = applicationContext.getBean(StoreDroneGroundOperationMapper.class);
        var groundOperation = mapper.selectByGroundOperationId(groundOperationId);
        groundOperation.setApplicationContext(applicationContext);
        return groundOperation;
    }

    public static StoreDroneGroundOperation build(String droneId, String runwayId) {
        if (applicationContext == null) return null;
        StoreDroneGroundOperationMapper mapper = applicationContext.getBean(StoreDroneGroundOperationMapper.class);
        var groundOperation = StoreDroneGroundOperation.builder().droneId(droneId).runwayId(runwayId).build();
        mapper.insert(groundOperation);
        return groundOperation;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        StoreDroneGroundOperation.applicationContext = applicationContext;
    }

    private static ApplicationContext applicationContext = null;
}
