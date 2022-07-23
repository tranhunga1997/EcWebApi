package com.example.useraccessdivide.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Predicate;


public class FileUtil {
	
	private FileUtil() {}
	
	public static boolean checkExtension(String fileName, Predicate<String> condition) {
		return condition.test(fileName);
	}
	
	public static byte[] readByte(File file) throws FileNotFoundException, IOException {
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			byte[] result = new byte[bis.available()];
			for(int i=0; i<result.length; i++) {
				result[i] = (byte) bis.read();
			}
			return result;
		}
	}
	
	public static void writeByte(File file, byte[] bytes) throws FileNotFoundException, IOException {
		try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
			bos.write(bytes);
		}
	}
}
