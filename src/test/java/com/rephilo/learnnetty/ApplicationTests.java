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

        //mock输入的byte
        String request = "request";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(request.getBytes());
        ByteBuf input = buf.duplicate();

        //输入消息写入inboundHandler
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

        //mock输出消息
        String response = "response";

        //输出消息写入outboundHandler
        assertTrue(channel.writeOutbound(response));
        assertTrue(channel.finish());

        //输出消息是否写入成功
        ByteBuf output = channel.readOutbound();
        assertNotNull(output);

        //读取输出的消息
        byte[] bytes = new byte[output.readableBytes()];
        int readerIndex = output.readerIndex();
        output.getBytes(readerIndex, bytes);

        //判断是否符合预期
        assertEquals(new String(bytes), response);
    }
}
