package com.w_angler.ethereum.explorer.utils;

import lombok.val;

/**
 * Created by wangle on 17-6-8.
 */
public class CheckUtil {
    private static final char[] HEX="abcdefABCDEF".toCharArray();

    /**
     * is valid hex?
     * @param str
     * @return
     */
    public static boolean isHex(String str){
        if(str.length()<=2){
            return false;
        }
        char[] chars=str.toCharArray();
        if(chars[0]!='0'||(chars[1]!='x'&&chars[1]!='X')){
            return false;
        }
        for(int i=2;i<chars.length;i++){
            if(!isHex(chars[i])){
                return false;
            }
        }
        return true;
    }
    private static boolean isHex(char ch){
        for(val c:HEX){
            if(ch==c){
                return true;
            }
        }
        return Character.isDigit(ch);
    }
}
