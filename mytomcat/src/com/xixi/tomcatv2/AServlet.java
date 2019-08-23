package com.xixi.tomcatv2;

import java.io.InputStream;
import java.io.OutputStream;

public class AServlet implements Servlet{

	@Override
	public void init() {
		System.out.println("init.....A");
		
	}

	@Override
	public void service(InputStream is, OutputStream ops) throws Exception {
		System.out.println("service.....A");
		ops.write("I from AServlet".getBytes());
	}

	@Override
	public void destory() {
		System.out.println("destory.....A");
		
	}

}
