package com.mycompany.javacard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JOptionPane;
import requestapplet.Request;

public class SmartCardWord {

    public static final byte[] AID_APPLET = {(byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x01};
    private Card card;
    private TerminalFactory factory;
    private CardChannel channel;
    private CardTerminal terminal;
    private List<CardTerminal> terminals;
    private ResponseAPDU response;
    private static SmartCardWord instance;

    // byte lay tu applet
    byte[] moduBytes;
    byte[] expoBytes;

    // dung de sinh pubkeyRSA
    BigInteger exponent;
    BigInteger modulus;

    PublicKey pub; // khai bao khoa : có thể gọi trực tiếp truyền vào lúc verify
    PublicKey pubKey;// khai báo để lưu vào file

    byte[] data_random;// random mảng byte truyền xuống để ký, sinh thẳng byte[] ngẫu nhiên đỡ convert
    byte[] signal;// chữ ký được gửi lên

    //sinh mảng byte ngẫu nhiên
    public static byte[] randomData(int lenbyte) {
        byte[] b = new byte[lenbyte];
        new Random().nextBytes(b);
        return b;
    }

    public static SmartCardWord getInstance() {
        if (instance == null) {
            instance = new SmartCardWord();
        }
        return instance;
    }

    public boolean connectCard() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=0");
            channel = card.getBasicChannel();
            if (channel == null) {
                return false;
            }
            response = channel.transmit(new CommandAPDU(0x00, (byte) 0xA4, 0x04, 0x00, AID_APPLET));
            String check = Integer.toHexString(response.getSW());
            if (check.equals("9000")) {

                System.out.println(response.getSW());
                return true;
            } else if (check.equals("6400")) {
                JOptionPane.showMessageDialog(null, "the bi vo hieu hoa");
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
        }
        return false;
    }

    public String getInforKT() {
        try {
            ResponseAPDU response = sendAPDU(0x00, 0x06, 1, 2, null);
            return new String(response.getData());
        } catch (CardException ex) {
            Logger.getLogger(SmartCardWord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String checkAuthen(String pass) {
        try {
            ResponseAPDU response = sendAPDU(0x00, 0X01, 0X01, 0X01,
                    pass.getBytes());
            String check = Integer.toHexString(response.getSW());
            return check;
        } catch (CardException ex) {
            Logger.getLogger(SmartCardWord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String initInfor(String inf) {
        try {
            ResponseAPDU response = sendAPDU(0x00, 0X03, 0X01, 0X01,
                    inf.getBytes());
            String check = Integer.toHexString(response.getSW());
            return check;
        } catch (CardException ex) {
            Logger.getLogger(SmartCardWord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String unlockCard() {
        ResponseAPDU response;
        try {
            response = sendAPDU(0x00, 0X02, 0X01, 0X01, null);
            String check = Integer.toHexString(response.getSW());
            return check;

        } catch (CardException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String updatePin(String pin) {
        ResponseAPDU response;
        try {
            response = sendAPDU(0x00, 0X05, 0X01, 0X01, pin.getBytes());
            String check = Integer.toHexString(response.getSW());
            return check;

        } catch (CardException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getInfo() {
        try {
            ResponseAPDU res = sendAPDU(0x00, 0X04, 0X01, 0X01, null);

            if (res.getSW1() == 0x90 && res.getSW2() == 0x00) {
                return new String(res.getData());
            }
        } catch (CardException ex) {
            Logger.getLogger(SmartCardWord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String updateInfor(String info) {
        System.out.println("updateInfor: info: " + info);
        ResponseAPDU response;
        try {
            response = sendAPDU(0x00, 0x07, 1,
                    2, info.getBytes());
            String check = Integer.toHexString(response.getSW());
            return check;

        } catch (CardException ex) {
            Logger.getLogger(SmartCardWord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String SetMoney(String money) {
        ResponseAPDU response;
        try {
            response = sendAPDU(0x00, 0x13, 0x12, 0x12, money.getBytes());
            return new String(response.getData());
        } catch (CardException ex) {
            Logger.getLogger(SmartCardWord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getMoney() {
        ResponseAPDU response;
        try {
            response = sendAPDU(0x00, 0x14, 0x12, 0x12, null);
            return new String(response.getData());
        } catch (CardException ex) {
            Logger.getLogger(SmartCardWord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    // 111 sinh khoá ở đây khoá pubkeyRSA có thể gọi trực tiếp để verify, ở đây lưu vào file luôn

    public PublicKey pubkeyRsa() {
        try {
            // lấy modu từ applet
            ResponseAPDU getmodu = sendAPDU(0x00, 0x10, 0x01, 0x01, null);
            moduBytes = getmodu.getData();
            modulus = new BigInteger(1, moduBytes);
            System.out.println("get modu " + modulus);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // lấy expo từ applet
            ResponseAPDU getex = sendAPDU(Request.CLA, 0X11, 0X01, 0X01, null);
            expoBytes = getex.getData();
            exponent = new BigInteger(1, expoBytes);
            System.out.println("get expo " + exponent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // sinh khoá pubRSA
            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            pub = factory.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // lưu vào file
        FileOutputStream fos = null;
        try {
            File publicKeyFile = createKeyFile(new File("publicKey.rsa"));
            fos = new FileOutputStream(publicKeyFile);
            fos.write(pub.getEncoded());
            fos.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
            }
        }
        // có thể gọi thẳng thằng này để verify
        return pub;
    }

    //check tạo file lưu pubRSA
    private static File createKeyFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }
        return file;
    }

    //xác thực Verify
    public boolean VerifyRsa() {

        //đọc file pubRSA
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("publicKey.rsa");
            byte[] b = new byte[fis.available()];
            fis.read(b);
            fis.close();

            // sinh khoá lại khi đọc từ file
            X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            pubKey = factory.generatePublic(spec);

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } catch (NoSuchAlgorithmException ex) {
        } catch (InvalidKeySpecException ex) {
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
            }
        }

        data_random = randomData(20);
        System.out.println("ram dom byte " + data_random);
        boolean isCorrect = false;
        try {
            // truyền thằng random byte[] xuống và lấy chữ ký lên
            ResponseAPDU sendverify = sendAPDU(Request.CLA, 0X12, 0X12, 0X12, data_random);
            signal = sendverify.getData();
            System.out.println("dau vao " + data_random);
            System.out.println("chu ky " + signal);

            Signature publicSignature = Signature.getInstance("SHA1withRSA");
            // pubRSA lấy từ file, có thể lấy thẳng thằng pubRSA ở trên cũng đc
            publicSignature.initVerify(pubKey);

            // đầu vào để xác thực, cái thằng byte[] đầu mà sinh ngẫu nhiên
            publicSignature.update(data_random);

            // verify truyền thằng chữ ký vào
            isCorrect = publicSignature.verify(signal);
            System.out.println("verify is " + isCorrect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("true or false " + isCorrect);
        if (isCorrect == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean disConnect() {
        try {
            card.disconnect(false);
            return true;
        } catch (Exception e) {

            System.out.println("Loi : " + e);
        }
        return false;
    }

    public ResponseAPDU sendAPDU(int cla, int ins, int p1, int p2, byte[] data) throws CardException {
        if (channel == null) {
            // throw new CardException(RequestResponse.SW_UNKNOWN);
        }

        return channel.transmit(new CommandAPDU(
                cla, ins, p1, p2, data));
    }

}
