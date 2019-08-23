package com.xixi;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * 模拟服务端向客户端返回数据
 * @author Asus
 *
 */
public class ServerTest {

	
	public static void main(String[] args) throws IOException {
		ServerSocket ssocket = null;
		Socket socket = null;
		OutputStream ops = null;
		try {
			ssocket = new ServerSocket(8081);
			while(true){
				socket = ssocket.accept();
				ops = socket.getOutputStream();
				ops.write("HTTP/1.1 200 OK\n".getBytes());
				ops.write("Content-Type: text/html;charset=utf-8\n".getBytes());
				ops.write("Server: BWM/1.1\n".getBytes());
				ops.write("\n\n".getBytes());
				StringBuffer sb = new StringBuffer();
				sb.append("<html><head><title>I am title.</title></head>");
				sb.append("<body><h1>I am H1</h1>");
				sb.append("<a href='https://www.baidu.com'>baidu.com</a></body></html>");
				ops.write(sb.toString().getBytes());
				ops.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ops != null) {
				ops.close();
				ops = null;
			}
			if(socket != null) {
				socket.close();
				socket = null;
			}
		}
	}
}
