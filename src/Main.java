import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main{
    public static void main(String [] args) throws IOException {
        Steganography.encode("kot.jpg","jezozwierz.png","kotekOut.png",2);
        Steganography.decode("kotekOut.png","odszyfrowane.png",2);
        //byte[] bFile = Files.readAllBytes(Paths.get("kotOdszyfrowany.png"));
        //System.out.println("0-owy bajt: "+ bFile[0]);
    }
}