package com.zivyou.learnpaxos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Proposal implements Serializable {
    Long requestId;
    String key;
    String value;
    Long responseId;

    public static Proposal fromMessage(String message) {
        String[] data = message.split(",");
        var str = data[3];
        Long responseId = "null".equals(str) ? null : Long.valueOf(str);
        return new Proposal(Long.valueOf(data[0]), data[1], data[2], responseId);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s", requestId, key, value, responseId);
    }
}
