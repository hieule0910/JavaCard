/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requestapplet;

/**
 *
 * @author ADMIN
 */
public interface Request {
    
    final public static byte CLA =(byte) 0x00;
    final public static byte VERIFY =(byte) 0X01;
    final public static byte P =(byte)0x63;
    final public static byte UPDATE = 0X03;
    final public static byte UNLOCK = 0X02;
 
    final public static byte LOCK = 0X04;
    
    
}
