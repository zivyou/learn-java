package com.zivyou.learnpaxos.controller;

import com.zivyou.learnpaxos.db.DataMap;
import com.zivyou.learnpaxos.service.Proposer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class OperateController {
    private final Proposer proposer;
    private final DataMap dataMap;
    @PostMapping("/put")
    public String put(@RequestParam String key, @RequestParam String value) {
        proposer.propose(key, value);
        return "success";
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return dataMap.get(key);
    }
}
