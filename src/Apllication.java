
import com.mycompany.javacard.GUI;
import com.mycompany.javacard.SmartCardWord;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class Apllication {
   public static GUI main ;
   public static SmartCardWord card ;
    public static void main(String[] args) {
             card = new SmartCardWord();
             main= new GUI();
             main.setVisible(true);
             main.setLocationRelativeTo(null);
             main.getContentPane().setBackground(new Color(255,204,204));
    }
    
    
    
   
}
