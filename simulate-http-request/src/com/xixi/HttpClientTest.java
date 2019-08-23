package com.xixi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 模拟浏览器获取服务端资源
 * @author Asus
 *
 */
public class HttpClientTest {

	public static void main(String[] args) throws IOException {
		Socket socket = null;
		InputStream is = null;
		OutputStream ops = null;
		try {
			// 1.建立socket连接到ip的指定端口
			socket = new Socket("www.baidu.com", 80);
			// 2.获取输入(输入到服务器)、输出流(输出到客户端)
			is = socket.getInputStream();
			ops = socket.getOutputStream();
			// 3.将Http协议的请求部分发送到服务端
			ops.write("GET / HTTP/1.1\n".getBytes());//     \n前面多一个空格都会导致请求出错
			ops.write("Host: www.baidu.com\n".getBytes());
			ops.write("\n".getBytes());
			// 4.读取相应数据打印到控制台
			int i = is.read();//read函数返回的是一个字节其在0~255中对应的数字
			while(i != -1) {
				System.out.print((char)i);
				i = is.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5.释放资源
			if(is != null) {
				is.close();
				is = null;
			}
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
