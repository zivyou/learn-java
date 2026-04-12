package com.zivyou.command;

import com.zivyou.result.Result;

public interface Command {
    Result execute();
    Result getResult();
}
