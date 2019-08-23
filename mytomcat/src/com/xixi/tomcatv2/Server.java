package com.xixi.tomcatv2;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 大致过程：
 * 1.建立服务端serversocket，使用accept方法监听请求，
 * 2.根据请求建立对应请求的socket
 * 3.读取socket的inputstream
 * 4.根据inputstream返回请求的资源，v2版本可以相应非静态资源
 * @author Asus
 *
 */
public class Server {

	private static String WEB_ROOT = System.getProperty("user.dir") + "\\webContent";
	private static String url = ""; //存放当前请求资源的名称
	
	
	private static Map<String, String> map = new HashMap<String, String>();
	
	
	
	
	
	
	//类加载时运行此静态代码块
	static {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(WEB_ROOT + "\\conf.properties"));
			Set set = prop.keySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = prop.getProperty(key);
				map.put(key, value);
			}
			//System.out.println(map);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		// System.out.println(WEB_ROOT);
		ServerSocket ssocket = null;
		Socket socket = null;
		InputStream is = null;
		OutputStream ops = null;
		try {
			ssocket = new ServerSocket(8080);
			while(true) {
				socket = ssocket.accept();
				is = socket.getInputStream();
				ops = socket.getOutputStream();
				//解析客户端的请求头内容
				parseIs(is);
				
				//判断访问的是什么类型的资源
				if(url.contains(".")) {
					//将对应的静态资源相应给客户端
					response(ops);
				}
				if(url.indexOf(".") == -1) {
					ops.write("HTTP/1.1 200 OK\n".getBytes());
					ops.write("\n".getBytes());
					//调用java小程序将对应的结果返回
					if(map.containsKey(url)) {
						Class clazz = Class.forName(map.get(url));
						Servlet servlet = (Servlet) clazz.newInstance();
						servlet.init();
						servlet.service(is, ops);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(ops != null) {
				ops.close();
				ops = null;
			}
			if(socket != null) {
				socket.close();
				socket = null;
			}
			if(is != null) {
				is.close();
				is = null;
			}
			
		}
	}

	//将对应的静态资源相应给客户端
	private static void response(OutputStream ops) throws IOException {
		byte[] buffer = new byte[2048];
		FileInputStream fis = null;
		try {
			File file = new File(WEB_ROOT, url);
			if(file.exists()) {
				ops.write("HTTP/1.1 200 OK\n".getBytes());
				ops.write("Server: BMW/1.1\n".getBytes());
				ops.write("Content-Type: text/html;charset=utf-8\n".getBytes());
				ops.write("\n".getBytes());
				fis = new FileInputStream(file);
				int i = fis.read(buffer);
				while(i != -1) {
					ops.write(buffer, 0, i);
					i = fis.read(buffer);
				}
			}else {
				ops.write("HTTP/1.1 404 NOT FOUND\n".getBytes());
				ops.write("Server: BMW/1.1\n".getBytes());
				ops.write("Content-Type: text/html;charset=utf-8\n".getBytes());
				ops.write("\n".getBytes());
				ops.write("file not found".getBytes());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(ops != null) {
				ops.close();
				ops = null;
			}
			if(fis != null) {
				fis.close();
				fis = null;
			}
		}
	}

	//解析客户端发送的请求头的内容，
	private static void parseIs(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer(2048);
		byte[] buffer = new byte[2048];
		int i = -1;
		i = is.read(buffer);
		for(int j = 0; j < i; j++) {
			sb.append((char)buffer[j]);
		}
		parseUrl(sb);
	}

	//将InputStream转换后的String的的内容中的资源名称截取出来存放在成员变量url中
	private static void parseUrl(StringBuffer sb) {
		int index1 = sb.indexOf(" ");
		int index2 = sb.indexOf(" ", index1+1);
		url = sb.substring(index1+2, index2);
	}
}
