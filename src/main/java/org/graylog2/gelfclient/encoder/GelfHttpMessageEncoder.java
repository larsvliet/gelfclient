package org.graylog2.gelfclient.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;
import org.graylog2.gelfclient.GelfConfiguration;

import java.util.List;

/**
 * A Netty channel handler which encodes messages to a {@link HttpRequest}.
 */
public class GelfHttpMessageEncoder extends MessageToMessageEncoder<ByteBuf> {

    private static final String CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String CONTENT_ENCODING = "gzip";

    private final GelfConfiguration config;

    public GelfHttpMessageEncoder(final GelfConfiguration config) {
        this.config = config;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, this.config.getPath(), buf.retain());
        request.headers().set(HttpHeaderNames.HOST, this.config.getHostname());
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_ENCODING, CONTENT_ENCODING);
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        out.add(request);
    }
}
