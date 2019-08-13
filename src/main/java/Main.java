import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main{
    public static void main(String [] args) throws IOException {
        Steganography.encode("D:\\@wakacjeNaJavie\\steganography\\kot.jpg","D:\\@wakacjeNaJavie\\steganography\\jezozwierz.png","D:\\@wakacjeNaJavie\\steganography\\kotekOut.png",2);
        Steganography.decode("D:\\@wakacjeNaJavie\\steganography\\kotekOut.png","D:\\@wakacjeNaJavie\\steganography\\odszyfrowane.png",2);
        //byte[] bFile = Files.readAllBytes(Paths.get("kotOdszyfrowany.png"));
        //System.out.println("0-owy bajt: "+ bFile[0]);
    }
}