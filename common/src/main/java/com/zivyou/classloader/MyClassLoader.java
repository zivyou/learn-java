package com.zivyou.classloader;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

public class MyClassLoader extends ClassLoader {
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        String file = "/home/youziqi/Documents/workspace/learn-java/common/target/classes/com/zivyou/service/ScheduleService.class";
        try (
                InputStream inputStream = new FileInputStream(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ) {
            System.out.println("input stream length: "+inputStream.available());
            int data;
            while ((data = inputStream.read()) != -1) {
                byteArrayOutputStream.write(data);
            }
            var bytes = byteArrayOutputStream.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Class<?> findClass(String moduleName, String name) {
        return super.findClass(moduleName, name);
    }
}
