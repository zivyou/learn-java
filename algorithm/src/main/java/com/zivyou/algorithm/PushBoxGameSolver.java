package com.zivyou.algorithm;

import java.util.*;

public class PushBoxGameSolver {

    // 方向数组：上、下、左、右
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final String[] DIR_NAMES = {"UP", "DOWN", "LEFT", "RIGHT"};

    /**
     * 求解推箱子游戏
     *
     * @param map        地图二维数组，1代表墙，0代表空闲的路
     * @param n          箱子和目标位置的数量
     * @param boxes      箱子初始位置数组 n*2
     * @param targets    目标位置数组 n*2
     * @param person     人的初始位置 1*2
     * @return          人的运动方向数组
     */
    public List<String> solve(int[][] map, int n, int[][] boxes, int[][] targets, int[] person) {
        int rows = map.length;
        int cols = map[0].length;

        // 将目标位置转换为Set便于快速查询
        Set<String> targetSet = new HashSet<>();
        for (int[] target : targets) {
            targetSet.add(target[0] + "," + target[1]);
        }

        // 初始状态：人位置 + 箱子位置集合
        State initialState = new State(person[0], person[1], boxes);
        Queue<State> queue = new LinkedList<>();
        Map<String, State> visited = new HashMap<>();

        queue.offer(initialState);
        visited.put(initialState.getKey(), initialState);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // 检查是否所有箱子都在目标位置
            if (isAllBoxesOnTargets(current.boxes, targetSet, n)) {
                return reconstructPath(current);
            }

            // 尝试向四个方向移动
            for (int dir = 0; dir < 4; dir++) {
                int newRow = current.personRow + DIRECTIONS[dir][0];
                int newCol = current.personCol + DIRECTIONS[dir][1];

                // 检查新位置是否在地图内且不是墙
                if (isValidPosition(newRow, newCol, rows, cols, map)) {
                    // 检查新位置是否有箱子
                    int boxIndex = findBoxAt(current.boxes, newRow, newCol);

                    if (boxIndex == -1) {
                        // 没有箱子，直接移动
                        State newState = new State(newRow, newCol, current.boxes);
                        String key = newState.getKey();

                        if (!visited.containsKey(key)) {
                            newState.parent = current;
                            newState.direction = dir;
                            visited.put(key, newState);
                            queue.offer(newState);
                        }
                    } else {
                        // 有箱子，尝试推动
                        int boxNewRow = newRow + DIRECTIONS[dir][0];
                        int boxNewCol = newCol + DIRECTIONS[dir][1];

                        // 检查箱子新位置是否有效
                        if (isValidPosition(boxNewRow, boxNewCol, rows, cols, map)
                                && !hasBoxAt(current.boxes, boxNewRow, boxNewCol)) {
                            // 创建新状态
                            int[][] newBoxes = copyBoxes(current.boxes);
                            newBoxes[boxIndex][0] = boxNewRow;
                            newBoxes[boxIndex][1] = boxNewCol;

                            // 排序箱子位置以确保状态唯一性
                            Arrays.sort(newBoxes, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

                            State newState = new State(newRow, newCol, newBoxes);
                            String key = newState.getKey();

                            if (!visited.containsKey(key)) {
                                newState.parent = current;
                                newState.direction = dir;
                                visited.put(key, newState);
                                queue.offer(newState);
                            }
                        }
                    }
                }
            }
        }

        // 无解
        return new ArrayList<>();
    }

    /**
     * 检查位置是否有效（在地图内且不是墙）
     */
    private boolean isValidPosition(int row, int col, int rows, int cols, int[][] map) {
        return row >= 0 && row < rows && col >= 0 && col < cols && map[row][col] == 0;
    }

    /**
     * 查找箱子在指定位置的索引
     */
    private int findBoxAt(int[][] boxes, int row, int col) {
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i][0] == row && boxes[i][1] == col) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 检查指定位置是否有箱子
     */
    private boolean hasBoxAt(int[][] boxes, int row, int col) {
        return findBoxAt(boxes, row, col) != -1;
    }

    /**
     * 检查所有箱子是否都在目标位置
     */
    private boolean isAllBoxesOnTargets(int[][] boxes, Set<String> targetSet, int n) {
        int count = 0;
        for (int[] box : boxes) {
            if (targetSet.contains(box[0] + "," + box[1])) {
                count++;
            }
        }
        return count == n;
    }

    /**
     * 复制箱子位置数组
     */
    private int[][] copyBoxes(int[][] boxes) {
        int[][] newBoxes = new int[boxes.length][2];
        for (int i = 0; i < boxes.length; i++) {
            newBoxes[i][0] = boxes[i][0];
            newBoxes[i][1] = boxes[i][1];
        }
        return newBoxes;
    }

    /**
     * 重构路径
     */
    private List<String> reconstructPath(State state) {
        List<String> path = new ArrayList<>();
        while (state.parent != null) {
            path.add(0, DIR_NAMES[state.direction]);
            state = state.parent;
        }
        return path;
    }

    /**
     * 状态类，表示游戏的一个状态
     */
    private static class State {
        int personRow;
        int personCol;
        int[][] boxes;
        State parent;
        int direction;  // 从父状态移动到这个状态的方向

        State(int personRow, int personCol, int[][] boxes) {
            this.personRow = personRow;
            this.personCol = personCol;
            this.boxes = boxes;
            this.parent = null;
            this.direction = -1;
        }

        /**
         * 生成唯一的状态键
         */
        String getKey() {
            StringBuilder sb = new StringBuilder();
            sb.append(personRow).append(",").append(personCol).append(";");
            for (int[] box : boxes) {
                sb.append(box[0]).append(",").append(box[1]).append(";");
            }
            return sb.toString();
        }
    }


}
