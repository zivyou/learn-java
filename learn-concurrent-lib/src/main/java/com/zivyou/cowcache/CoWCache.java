package com.zivyou.cowcache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public final class CoWCache {
    private final Map<String, CopyOnWriteArraySet<AssetInfo>> cache = new ConcurrentHashMap<>();


    public Set<AssetInfo> getByCircleCode(String code) {
        return cache.get(code);
    }

    public void remove(AssetInfo assetInfo) {
        Set<AssetInfo> set = cache.get(assetInfo.getCircleCode());
        if (set != null) {
            set.remove(assetInfo);
        }
    }

    public void add(AssetInfo assetInfo) {
        CopyOnWriteArraySet<AssetInfo> set = cache.get(assetInfo.getCircleCode());
        if (set == null) {
            set = new CopyOnWriteArraySet<>();
            cache.put(assetInfo.getCircleCode(), set);
        }
        set.add(assetInfo);
    }
}
