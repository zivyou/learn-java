package com.zivyou;

import com.zivyou.classloader.MyClassLoader;
import com.zivyou.service.ScheduleService;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println("Hello world!");

        MyClassLoader classLoader = new MyClassLoader();
        Class<ScheduleService> clazz = (Class<ScheduleService>) classLoader.findClass("com.zivyou.service.ScheduleService");
        ScheduleService service = (ScheduleService)clazz.getConstructor().newInstance();
        System.out.println(service);
    }
}