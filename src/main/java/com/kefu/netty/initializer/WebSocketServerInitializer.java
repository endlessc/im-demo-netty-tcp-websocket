package com.kefu.netty.initializer;

import com.kefu.netty.codec.WebSocketPacketCodec;
import com.kefu.netty.util.PipelineUtil;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * websocket连接初始化Channel，给Channel关联的pipeline添加handler
 *
 * @author feng
 * @date 2019-04-22
 */
@Component
public class WebSocketServerInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 处理第一次连接http的握手请求
        pipeline.addLast(new HttpServerCodec());
        // 写文件内容
        pipeline.addLast(new ChunkedWriteHandler());
        // 保证接收的http请求的完整性
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        // 处理其他的WebSocketFrame
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));

        // 空闲检测
//        ch.pipeline().addLast(new IMIdleStateHandler());

        // WebSocket数据包编解码器
        ch.pipeline().addLast(WebSocketPacketCodec.INSTANCE);

        // 添加tcp/websocket通用handler
        PipelineUtil.addHandler(pipeline);
    }
}
