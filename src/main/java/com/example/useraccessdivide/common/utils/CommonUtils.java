package com.example.useraccessdivide.common.utils;

import java.text.Normalizer;


import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Class chung xử lý logic
 * @author tranh
 *
 */
public class CommonUtils {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    
    /**
     * <dd>Giải thích: ngăn không cho tạo instance
     */
    private CommonUtils(){}
    
    /**
     * <dd>Giải thích: chuyển sang chuỗi ký tự dạng slug
     * <dd>Ví dụ: hello world -> hello-world
     * @param input
     * @return String
     */
    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
    
    /**
     * <dd>Giải thích: kiểm tra số tự nhiên
     * @param nums
     * @return <code>true</code> là chuỗi số nguyên, <code>false</code> ngược lại
     */
    public static boolean isNumber(String... nums) {
    	String pattern = "^\\d+$";
    	if(isNull((Object)nums) || isBlank(nums)) {
    		return false;
    	}
    	
    	for(String num : nums) {
        	if(!Pattern.matches(pattern, num)) {
        		return false;
        	}
    	}
    	return true;
    }
    /**
     * <dd>Giải thích: kiểm tra chuỗi null
     * @param objs
     * @return <code>true</code> chuỗi null, <code>false</code> ngược lại
     */
    public static boolean isNull(Object... objs) {
    	for(Object obj:objs) {
    		if(obj != null) {
    			return false;
    		}
    	}
    	return true;
    }
    /**
     * <dd>Giải thích: kiểm tra chuỗi trống và ký tự khoảng trắng
     * @param strs
     * @return <code>true</code> chuỗi trống, <code>false</code> ngược lại
     */
    public static boolean isBlank(String...strs) {
    	for(String str:strs) {
    		if(!"".equals(str) || !" ".equals(str)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * <dd>Giải thích: định dạng số kiểu Việt Nam
     * <dd>Tham khảo: 1000000 -> 1,000,000
     * @param num
     * @return số đã định dạng
     */
    public static String numberFormat(long num) {
    	Locale en = new Locale("en","EN");
    	NumberFormat format = NumberFormat.getInstance(en);
    	String numberFormatted = format.format(num);
    	return numberFormatted;
    }
    
}
