package com.xixi.tomcatv2;

import java.io.InputStream;
import java.io.OutputStream;

public class BServlet implements Servlet{

	@Override
	public void init() {
		System.out.println("init.....B");
		
	}

	@Override
	public void service(InputStream is, OutputStream ops) throws Exception {
		System.out.println("service.....B");
		ops.write("I from BServlet".getBytes());
	}

	@Override
	public void destory() {
		System.out.println("destory.....B");
		
	}

}
