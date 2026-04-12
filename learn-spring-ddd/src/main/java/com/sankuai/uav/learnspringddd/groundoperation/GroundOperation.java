package com.sankuai.uav.learnspringddd.groundoperation;

public interface GroundOperation {
    void start();
    void block(Long time);
    void failed(Integer reason);
    void complete();
}
