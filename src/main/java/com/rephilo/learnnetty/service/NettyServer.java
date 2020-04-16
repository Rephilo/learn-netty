package com.rephilo.learnnetty.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.plugin2.message.Serializer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author rephilo
 */
@Component
public class NettyServer {
    /**
     * boss 线程组用于处理连接工作
     */
    private EventLoopGroup boss = new NioEventLoopGroup();
    /**
     * work 线程组用于数据处理
     */
    private EventLoopGroup worker = new NioEventLoopGroup();

    @Value("${netty.port}")
    private Integer port;

    /**
     * 初始化方法
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void init() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap
                //处理 Channel 事件的 EventLoopGroup
                .group(boss, worker)
                //Channel类型
                .channel(NioServerSocketChannel.class)
                //绑定端口
                .localAddress(port)
                // 处理链接 handler
//                .handler()
                //处理数据 child handler
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast();
                    }
                });
        //同步启动
        bootstrap.bind().sync();
        System.out.println("启动Netty");
    }

    /**
     * 销毁方法
     *
     * @throws InterruptedException
     */
    @PreDestroy
    public void destroy() throws InterruptedException {
        boss.shutdownGracefully().sync();
        worker.shutdownGracefully().sync();
        System.out.println("关闭Netty");
    }
}
