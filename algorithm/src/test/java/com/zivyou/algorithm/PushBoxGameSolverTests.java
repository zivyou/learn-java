package com.zivyou.algorithm;

import org.junit.jupiter.api.Test;

import java.util.*;

public class PushBoxGameSolverTests {

    @Test
    public void test01() {
        // 简单测试用例
        // 地图：
        // 1 1 1 1 1
        // 1 0 0 0 1
        // 1 0 P 0 1
        // 1 0 B T 1
        // 1 1 1 1 1
        int[][] map = {
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1}
        };
        int n = 1;
        int[][] boxes = {{2, 2}};
        int[][] targets = {{2, 3}};
        int[] person = {1, 2};

        List<String> result = new PushBoxGameSolver().solve(map, n, boxes, targets, person);
        System.out.println("解决方案: " + result);
    }

    @Test
    public void test02() {
        // 更复杂的测试用例 - 两个箱子
        int[][] map = {
                {1, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 1, 1},
                {1, 1, 0, 1, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 1},
                {1, 0, 1, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1}
        };
        int n = 3;
        int[][] boxes = {{2, 4}, {3, 2}, {4, 4}};
        int[][] targets = {{1, 4}, {4, 5}, {5, 2}};
        int[] person = {5, 5};

        List<String> result = new PushBoxGameSolver().solve(map, n, boxes, targets, person);
        System.out.println("解决方案: " + result);
    }
}
