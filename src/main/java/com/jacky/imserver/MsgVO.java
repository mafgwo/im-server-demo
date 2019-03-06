package com.jacky.imserver;

import lombok.Data;

@Data
public class MsgVO {

    /**
     * 1 群聊 8 用户加入群聊消息 9 群聊系统通知
     */
    private Integer type;

    private String fromName;

    private String fromId;

    private String msg;

    private String sendTime;
}
