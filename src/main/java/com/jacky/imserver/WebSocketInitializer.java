package com.jacky.imserver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketInitializer extends ChannelInitializer {


    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        // 对大数据流对支持
        pipeline.addLast("ChunkedWriteHandler", new ChunkedWriteHandler());

        // http对支持
        pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(1024 * 64));

        pipeline.addLast("WebSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast("ChatHandler", new ChatHandler());



    }
}
