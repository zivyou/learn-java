package com.zivyou.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class HungarianTests {

    /**
     * 测试数据结构
     */
    static class TestData {
        int leftN;
        int rightN;
        List<int[]> edges;  // 每个元素是 [leftNode, rightNode]
        int expectedMaxMatching;

        TestData(int leftN, int rightN, int expectedMaxMatching) {
            this.leftN = leftN;
            this.rightN = rightN;
            this.edges = new ArrayList<>();
            this.expectedMaxMatching = expectedMaxMatching;
        }

        TestData addEdge(int left, int right) {
            edges.add(new int[]{left, right});
            return this;
        }
    }

    /**
     * 创建 Hungarian 实例并填充测试数据
     */
    private Hungarian createHungarian(TestData data) {
        Hungarian hungarian = new Hungarian();
        hungarian.leftN = data.leftN;
        hungarian.rightN = data.rightN;
        hungarian.graph = new int[data.leftN + 1][data.rightN + 1];

        for (int[] edge : data.edges) {
            int left = edge[0];
            int right = edge[1];
            hungarian.graph[left][right] = 1;
        }

        return hungarian;
    }

    @Test
    void testBasicMatching() {
        // 左侧: 1,2,3  右侧: 1,2,3
        // 边: 1-1, 1-2, 2-2, 2-3
        // 预期最大匹配: 2
        TestData data = new TestData(3, 3, 2)
                .addEdge(1, 1)
                .addEdge(1, 2)
                .addEdge(2, 2)
                .addEdge(2, 3);

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(2, result, "基础测试：预期最大匹配数为2");
    }

    @Test
    void testPerfectMatching() {
        // 左侧: 1,2,3  右侧: 1,2,3
        // 边: 1-1, 1-2, 2-2, 2-3, 3-3, 3-1
        // 预期最大匹配: 3 (完全匹配)
        TestData data = new TestData(3, 3, 3)
                .addEdge(1, 1)
                .addEdge(1, 2)
                .addEdge(2, 2)
                .addEdge(2, 3)
                .addEdge(3, 3)
                .addEdge(3, 1);

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(3, result, "完整匹配测试：预期最大匹配数为3");
    }

    @Test
    void testAugmentingPath() {
        // 链式结构，需要通过增广路径找到最优解
        // 左侧: 1,2,3,4  右侧: 1,2,3,4
        // 边: 1-1, 2-1, 2-2, 3-2, 3-3, 4-3, 4-4
        // 预期最大匹配: 4
        TestData data = new TestData(4, 4, 4)
                .addEdge(1, 1)
                .addEdge(2, 1)
                .addEdge(2, 2)
                .addEdge(3, 2)
                .addEdge(3, 3)
                .addEdge(4, 3)
                .addEdge(4, 4);

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(4, result, "增广路径测试：预期最大匹配数为4");
    }

    @Test
    void testMoreRightNodes() {
        // 右侧节点多于左侧
        // 左侧: 1,2  右侧: 1,2,3,4
        // 边: 1-1, 1-2, 1-3, 2-2, 2-3, 2-4
        // 预期最大匹配: 2 (受限于左侧节点数)
        TestData data = new TestData(2, 4, 2)
                .addEdge(1, 1)
                .addEdge(1, 2)
                .addEdge(1, 3)
                .addEdge(2, 2)
                .addEdge(2, 3)
                .addEdge(2, 4);

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(2, result, "右侧节点更多测试：预期最大匹配数为2");
    }

    @Test
    void testCannotFullyMatch() {
        // 右侧节点少于左侧
        // 左侧: 1,2,3  右侧: 1,2
        // 边: 1-1, 2-1, 2-2, 3-2
        // 预期最大匹配: 2 (受限于右侧节点数)
        TestData data = new TestData(3, 2, 2)
                .addEdge(1, 1)
                .addEdge(2, 1)
                .addEdge(2, 2)
                .addEdge(3, 2);

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(2, result, "无法完全匹配测试：预期最大匹配数为2");
    }

    @Test
    void testEmptyGraph() {
        // 空图，没有边
        // 左侧: 3  右侧: 3
        // 预期最大匹配: 0
        TestData data = new TestData(3, 3, 0);

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(0, result, "空图测试：预期最大匹配数为0");
    }

    @Test
    void testCompleteBipartiteGraph() {
        // 完全二分图 K3,3
        // 左侧: 1,2,3  右侧: 1,2,3
        // 每个左侧节点连接所有右侧节点
        // 预期最大匹配: 3
        TestData data = new TestData(3, 3, 3);
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                data.addEdge(i, j);
            }
        }

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(3, result, "完全二分图测试：预期最大匹配数为3");
    }

    @Test
    void testSingleMatch() {
        // 只有一条边
        // 左侧: 2  右侧: 2
        // 边: 1-1
        // 预期最大匹配: 1
        TestData data = new TestData(2, 2, 1)
                .addEdge(1, 1);

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(1, result, "单条边测试：预期最大匹配数为1");
    }

    @Test
    void testDisconnectedComponents() {
        // 不连通的组件
        // 左侧: 4  右侧: 4
        // 边: 1-1, 1-2, 3-3, 3-4, 4-3, 4-4
        // 预期最大匹配: 3 (1-2, 3-3, 4-4 或其他组合)
        TestData data = new TestData(4, 4, 3)
                .addEdge(1, 1)
                .addEdge(1, 2)
                .addEdge(3, 3)
                .addEdge(3, 4)
                .addEdge(4, 3)
                .addEdge(4, 4);

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(3, result, "不连通组件测试：预期最大匹配数为3");
    }

    @Test
    void testStarGraph() {
        // 星形图：所有左侧节点都连接同一个右侧节点
        // 左侧: 4  右侧: 2
        // 边: 1-1, 2-1, 3-1, 4-1, 4-2
        // 预期最大匹配: 2 (1-1, 4-2)
        TestData data = new TestData(4, 2, 2)
                .addEdge(1, 1)
                .addEdge(2, 1)
                .addEdge(3, 1)
                .addEdge(4, 1)
                .addEdge(4, 2);

        Hungarian hungarian = createHungarian(data);
        int result = hungarian.solve();
        assertEquals(2, result, "星形图测试：预期最大匹配数为2");
    }
}
