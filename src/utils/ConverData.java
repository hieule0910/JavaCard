/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author ADMIN
 */
public class ConverData {
      public static byte[] stringToByteArray(String string){
        char[] chars = string.toCharArray();
        return charArrayToByteArray(chars);
    }
    
    public static byte[] charArrayToByteArray(char[] chars){
        int len = chars.length;
        byte[] bytes = new byte[len];
         for(int ipIndx = 0; ipIndx<chars.length;ipIndx++){
            bytes[ipIndx] = (byte)(chars[ipIndx]&0x0F);
        }
         
         return bytes;
    }
      public String atrToHex(byte atCode) {
        StringBuilder result = new StringBuilder();
            result.append(String.format("%02x", atCode));
        return result.toString();
    }
    public String shorttoHex(short atCode) {
        StringBuilder result = new StringBuilder();
            result.append(String.format("%02x", atCode));
        return result.toString();
    }
}
