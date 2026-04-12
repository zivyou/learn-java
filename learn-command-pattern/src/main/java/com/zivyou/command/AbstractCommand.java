package com.zivyou.command;

import com.zivyou.receiver.Receiver;
import com.zivyou.result.Result;

public abstract class AbstractCommand implements Command {
    private Receiver receiver;
    private Result result = null;
    public AbstractCommand(Receiver receiver) {
        this.receiver = receiver;
    }
}
