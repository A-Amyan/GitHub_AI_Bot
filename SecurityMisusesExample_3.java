import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.SecureRandom;
import java.util.Random;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.KeyStore;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;

public class SecurityMisusesExample {
    public static void main(String[] args) throws Exception {
        // 1) Hardcoded cryptographic key in SecretKeySpec
        String hardcodedKey = "1234567812345678"; // Insecure: Hardcoded key
        SecretKeySpec keySpec = new SecretKeySpec(hardcodedKey.getBytes(), "AES");

        // 11) Using ECB mode (insecure)
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); 
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // 2) Hardcoded password in PBEKeySpec
        char[] hardcodedPassword = "hardcodedPassword".toCharArray(); // Insecure: Hardcoded password
        byte[] salt = "12345678".getBytes(); // 10) Constant salt (insecure)
        int iterationCount = 500; // 13) Iteration count < 1000 (insecure)
        PBEKeySpec pbeKeySpec = new PBEKeySpec(hardcodedPassword, salt, iterationCount);

        // 3) Hardcoded KeyStore password
        String keystorePassword = "hardcodedKeystorePassword"; // Insecure: Hardcoded password
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, keystorePassword.toCharArray());

        // 4) HostnameVerifier always returning true
        HostnameVerifier hostnameVerifier = (hostname, session) -> true; 
        URL url = new URL("http://example.com"); // 7) Using HTTP instead of HTTPS
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setHostnameVerifier(hostnameVerifier);

        // 5) X509TrustManager with empty certificate validation
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
        };

        // 6) SSLSocket with omitted hostname verification (we rely on the empty trust manager from above)
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        SSLSocketFactory factory = (SSLSocketFactory) sslContext.getSocketFactory();
        SSLSocket sslSocket = (SSLSocket) factory.createSocket("example.com", 443);
        // No explicit hostname verification here:
        sslSocket.startHandshake();

        // 8) Using java.util.Random instead of SecureRandom
        Random insecureRandom = new Random();
        int randomValue = insecureRandom.nextInt();

        // 9) Using constant seed in SecureRandom
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(12345L); // Insecure: Constant seed

        // 14) Using broken symmetric cipher (DES instead of AES)
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); // Insecure: DES is broken
        SecretKeySpec desKeySpec = new SecretKeySpec("12345678".getBytes(), "DES");
        desCipher.init(Cipher.ENCRYPT_MODE, desKeySpec);

        // 15) Using broken RSA key size < 2048 bits
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024); // Insecure: 1024-bit RSA is considered weak
        KeyPair keyPair = keyGen.generateKeyPair();

        // 16) Using a broken hash function (SHA1)
        MessageDigest sha1Digest = MessageDigest.getInstance("SHA1"); // Insecure: SHA1 is broken
        sha1Digest.update("example".getBytes());
        byte[] hash = sha1Digest.digest();

        System.out.println("All 16 insecure operations performed.");
    }
}
