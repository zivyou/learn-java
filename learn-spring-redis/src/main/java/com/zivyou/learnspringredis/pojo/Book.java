package com.zivyou.learnspringredis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 20230718L;
    String id;
    String name;
}
