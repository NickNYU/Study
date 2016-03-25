package com.emc.ehc.nick.netty.Http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private final String url;
	private static final String BAD_REQUEST = "Bad Request";
	private static final String METHOD_NOT_ALLOWED = "Method not allowed";
	private static final HttpMethod GET = HttpMethod.GET;
	private static final String FORBIDEN = "Path Forbidened";
	private static final String NOT_FOUND = "Not Found";
	
	public HttpFileServerHandler(String url) {
		this.url = url;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if(!request.getDecoderResult().isSuccess()) {
			sendError(ctx, BAD_REQUEST);
			return;
		}
		
		if(request.getMethod() != GET) {
			sendError(ctx, METHOD_NOT_ALLOWED);
			return;
		}
		
		final String url = request.getUri();
		final String path = sanitizeUri(url);
		
		if(path == null) {
			sendError(ctx, FORBIDEN);
			return;
		}
		
		File file = new File(path);
		
		RandomAccessFile randomAccessFile = null;
		
		try {
			randomAccessFile = new RandomAccessFile(file, "r");
		} catch(FileNotFoundException e) {
			sendError(ctx, NOT_FOUND);
			return;
		}
		
		long fileLength = randomAccessFile.length();
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}
	
	private String sanitizeUri(String url) {
		return null;
	}

	private void sendError(ChannelHandlerContext ctx, String err) {
		
	}

}
