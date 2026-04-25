package com.zivyou.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

// ref: https://oi-wiki.org/graph/graph-matching/bigraph-weight-match/
@Slf4j
public class KuhnMunkres {
    int leftN, rightN;
    int[][] graph;
    int[] pair;

    int[] lx, ly;
    int[] slack; // 加速计算使用的中间变量
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
            int weight = scanner.nextInt();
            graph[left][right] = weight;
        }
    }

    private boolean dfs(int leftNode, boolean[] leftVisited, boolean[] rightVisited) {
        leftVisited[leftNode] = true;
        for (int rightNode = 0; rightNode < rightN; rightNode++) {
            if (rightVisited[rightNode]) continue;

            int gap = lx[leftNode] + ly[rightNode] - graph[leftNode][rightNode];
            if (graph[leftNode][rightNode] > 0 && gap == 0) {
                rightVisited[rightNode] = true;
                if (pair[rightNode] == -1 || dfs(pair[rightNode], leftVisited, rightVisited)) {
                    pair[rightNode] = leftNode;
                    return true;
                }
            } else {
                // 更新slack值，记录需要调整的最小顶标量
                // 只有 gap > 0 时才更新，gap == 0 说明没有边或已经满足条件，不需要调整
                if (gap > 0) {
                    slack[rightNode] = Math.min(gap, slack[rightNode]);
                }
            }
        }
        return false;
    }

    public int solve() {
        // 初始化数组
        lx = new int[leftN];
        ly = new int[rightN];
        pair = new int[rightN];
        slack = new int[rightN];

        // 初始化pair数组为-1（表示未匹配）
        for (int i = 0; i < rightN; i++) {
            pair[i] = -1;
        }

        // 初始化lx为每行的最大值，ly为0
        for (int i = 0; i < leftN; i++) {
            int maxLx = 0;
            for (int j = 0; j < rightN; j++) {
                if (graph[i][j] < 0) {
                    graph[i][j] = 0;
                }
                maxLx = Math.max(graph[i][j], maxLx);
            }
            lx[i] = maxLx;
        }

        int pairCount = 0;
        for (int i = 0; i < leftN; i++) {
            boolean[] leftVisited = new boolean[leftN];
            boolean[] rightVisited = new boolean[rightN];

            // 初始化slack数组
            for (int j = 0; j < rightN; j++) {
                slack[j] = Integer.MAX_VALUE;
            }

            while (true) {
                if (dfs(i, leftVisited, rightVisited)) {
                    pairCount++;
                    break;
                }

                // 计算delta：在所有未访问的右侧节点中找最小的slack值
                int delta = Integer.MAX_VALUE;
                boolean found = false;
                for (int j = 0; j < rightN; j++) {
                    if (!rightVisited[j] && slack[j] < Integer.MAX_VALUE) {
                        delta = Math.min(slack[j], delta);
                        found = true;
                    }
                }

                // 如果没有找到有效的delta，说明无法继续匹配
                if (!found || delta == Integer.MAX_VALUE) {
                    break;
                }
                log.info("delta={}", delta);
                // 调整顶标
                for (int k = 0; k < leftN; k++) {
                    if (leftVisited[k]) {
                        lx[k] -= delta;
                    }
                }
                for (int k = 0; k < rightN; k++) {
                    if (rightVisited[k]) {
                        ly[k] += delta;
                    }
                }

                // 顶标调整后，新的等边可能出现，需要重置右侧访问状态
                // 但保留左侧访问状态（只扩展已访问的左侧节点集合）
                rightVisited = new boolean[rightN];
                for (int j = 0; j < rightN; j++) {
                    slack[j] = Integer.MAX_VALUE;
                }
            }
        }
        return pairCount;
    }
}
