package com.gmx.mattcha.dynmaparea.util;

import java.util.HashMap;
import java.util.Map;

public class BaseCustomMessage {

    protected Map<String, String> messages = new HashMap<>();

    protected String unknownKeyMsg = "Unknown Key: %0";

    public BaseCustomMessage(Map<String, String> msgs) {
        this.messages = msgs;
    }

    public BaseCustomMessage() {
    }

    protected String replaceMessage(String msg, String ...args) {
        if (args == null) {
            return msg;
        }

        for (int i = 0; i < args.length; i++) {
            String k = "%" + i;
            String v = args[i];

            msg = msg.replace(k, v);
        }

        return msg;
    }

    // API

    public Map<String, String> getAllMessages() {
        return messages;
    }

    public String getUnknownKeyMsg() {
        return unknownKeyMsg;
    }

    public void setUnknownKeyMsg(String msg) {
        this.unknownKeyMsg = msg;
    }

    public boolean hasMessage(String key) {
        return this.messages.containsKey(key);
    }

    public String getMessage(String key, String ...args) {
        if (!this.hasMessage(key)) {
            return this.replaceMessage(unknownKeyMsg, key);
        }

        return this.replaceMessage(this.messages.get(key), args);
    }

    public void setMessage(String key, String msg) {
        this.messages.put(key, msg);
    }

    public void fillMessage(String key, String msg) { // if exists, not set.
        if (!this.hasMessage(key)) {
            return;
        }

        this.messages.put(key, msg);
    }

    public void fillMessages(Map<String, String> msgs) { // if exists, not set.
        for (Map.Entry<String, String> e : msgs.entrySet()) {
            this.fillMessage(e.getKey(), e.getValue());
        }
    }
}