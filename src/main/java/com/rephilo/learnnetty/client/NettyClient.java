package com.rephilo.learnnetty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.channels.SocketChannel;

@DependsOn("nettyServer")
@Component
public class NettyClient {

    private EventLoopGroup group = new NioEventLoopGroup();

    @Value("${netty.port}")
    private int port;

    @Value("${netty.host}")
    private String host;

    @PostConstruct
    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                //处理 Channel 所有事件的 EventLoopGroup
                .group(group)
                //Channel类型
                .channel(NioSocketChannel.class)
                //远程/本地的 ip&端口
//                .localAddress(port)
                .remoteAddress(host, port)
                //channelOption配置项，只初始化一次，不可修改
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                //配置 Channel 的属性值，，只初始化一次，不可修改
//                .attr(AttributeKey.)
                //配置channelHandler
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast();
                    }
                });

        //bind是监听远程端口，需要再调用connect方法进行连接
//        bootstrap.bind().channel().connect();

        //链接到Server
        //connect() = bind() + channel.connect()
        ChannelFuture channelFuture = bootstrap.connect();
        //增加监听
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("启动Netty Client");
            }
        });

        //同步方式
//        bootstrap.connect().sync();
    }
}
