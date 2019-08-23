package com.xixi.tomcatv2;

import java.io.InputStream;
import java.io.OutputStream;

public interface Servlet {

	
	public void init();
	public void service(InputStream is, OutputStream ops) throws Exception;
	public void destory();
	
}
