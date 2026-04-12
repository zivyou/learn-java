package com.zivyou.learnzookeeper.service;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZookeeperUtils {
    private static final String s = "/";

    private final CuratorFramework curatorFramework;

    public String createNode(String path, String node) {
        return createNode(path, node, CreateMode.PERSISTENT);
    }

    public String createNode(String p, String node, CreateMode createMode) {
        String path = buildPath(p, node);
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(createMode)
                    .forPath(path);
            return path;
        } catch (Exception e) {
            log.error("create node failed: path: {}", path, e);
        }
        return null;
    }

    private String buildPath(String p, String node) {
        StringBuilder path = new StringBuilder("");
        if (Strings.isNullOrEmpty(p) || Strings.isNullOrEmpty(node)) {
            log.error("ZooKeeper路径或者节点不能为空!");
        }
        if (!p.startsWith(s)) {
            path.append(s).append(p);
        }
        if (s.equals(path.toString())) {
            return path + node;
        } else {
            return path + s + node;
        }
    }
    public String createNode(String p, String node, String value) {
        return createNode(p, node, value, CreateMode.PERSISTENT);
    }
    public String createNode(String p, String node, String value, CreateMode createMode) {
        String path = buildPath(p, node);
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(createMode)
                    .forPath(path, value.getBytes());
            return path;
        } catch (Exception e) {
            log.error("create node failed: path: {}", path, e);
        }
        return null;
    }

    public String get(String p, String node) {
        String path = buildPath(p, node);
        return null;
    }
}
