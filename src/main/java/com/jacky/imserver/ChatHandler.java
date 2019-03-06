package com.jacky.imserver;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        System.out.println("服务器收到消息: " + content);

        MsgVO fromMsgVO = JSON.parseObject(content, MsgVO.class);
        // 如果是加入群聊
        if (fromMsgVO.getType() == 8) {
            // 发送系统通知有人加入群聊
            MsgVO msgVO = new MsgVO();
            msgVO.setType(9);
            msgVO.setMsg("用户" + fromMsgVO.getFromName() + "加入群聊");
            msgVO.setSendTime(DateFormatUtils.format(new Date(), "yy-MM-dd HH:mm:ss"));
            // 真实处理情况 这里会加入某个群组中去
            clients.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgVO)));
        } else {
            fromMsgVO.setSendTime(DateFormatUtils.format(new Date(), "yy-MM-dd HH:mm:ss"));
            // 真实情况 这里仅仅是发给某个群组 而不是所有连接到通道
            clients.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(fromMsgVO)));
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        clients.add(ctx.channel());
        System.out.println("加入channel: " + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        System.out.println("移除channel: " + ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
