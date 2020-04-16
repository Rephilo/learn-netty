package com.rephilo.learnnetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testInbound() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
        channel.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));

        //mock传入的byte
        String request = "request";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(request.getBytes());
        ByteBuf input = buf.duplicate();

        //接收消息
        assertTrue(channel.writeInbound(input));
        assertTrue(channel.finish());

        //经过inboundHandler解析成业务需要的内容
        String msg = channel.readInbound();
        assertEquals(request, msg);

        //是否读取完毕
        assertNull(channel.readInbound());
    }

    @Test
    public void testOutbound() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
        channel.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));

        //mock传入的byte
        String response = "response";

        //经过outboundHandler编码
        assertTrue(channel.writeOutbound(response));
        assertTrue(channel.finish());

        //是否编码成功
        assertNotNull(channel.readOutbound());
    }
}
