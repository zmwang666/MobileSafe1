package com.itheima.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class StreamUtils {
	public static String readToString(InputStream is) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[1024];
		
		while((len = is.read(buffer)) != -1){
			out.write(buffer, 0, len);
		}
		
		String result = out.toString();
		is.close();
		out.close();
		
		return result;
		
	}
}
