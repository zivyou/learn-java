package com.zivyou.algorithm;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 匈牙利算法:求解最大二分图匹配问题
 */

public class Hungarian {
    int[][] graph;
    int leftN;   // 左侧节点数量
    int rightN;  // 右侧节点数量
    int[] pair;  // pair[j] = i 表示右侧节点j匹配到左侧节点i

    public void input() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入左侧节点数量: ");
        leftN = scanner.nextInt();
        System.out.print("请输入右侧节点数量: ");
        rightN = scanner.nextInt();
        System.out.print("请输入边的数量: ");
        int E = scanner.nextInt();

        graph = new int[leftN + 1][rightN + 1];
        System.out.println("请输入每一条边(格式: 左侧节点 右侧节点):");
        for (int i = 0; i < E; i++) {
            int left = scanner.nextInt();
            int right = scanner.nextInt();
            graph[left][right] = 1;
        }
    }

    public int solve() {
        pair = new int[rightN + 1];
        Arrays.fill(pair, -1);

        int maxMatching = 0;
        for (int leftNode = 1; leftNode <= leftN; leftNode++) {
            boolean[] visited = new boolean[rightN + 1];
            if (dfs(leftNode, visited)) {
                maxMatching++;
            }
        }
        return maxMatching;
    }

    private boolean dfs(int leftNode, boolean[] visited) {
        for (int rightNode = 1; rightNode <= rightN; rightNode++) {
            if (graph[leftNode][rightNode] > 0 && !visited[rightNode]) {
                visited[rightNode] = true;

                // 如果右侧节点未匹配，或者能为已匹配的左侧节点找到新匹配
                if (pair[rightNode] == -1 || dfs(pair[rightNode], visited)) {
                    pair[rightNode] = leftNode;
                    return true;
                }
            }
        }
        return false;
    }
}
