package com.zivyou;

import com.zivyou.house.House;
import com.zivyou.house.MyHouse;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        House myHouse = new MyHouse(10, 0);
        myHouse.open();
    }
}