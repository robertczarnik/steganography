import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class SteganographyTest {
    private int bitsPetPixel;
    private int encodeType;
    private String base;
    private String secret;
    private String suffix;

    public SteganographyTest(int bitsPerPixel, int encodeType, String base, String secret, String suffix){
        this.bitsPetPixel=bitsPerPixel;
        this.encodeType=encodeType;
        this.base=base;
        this.secret=secret;
        this.suffix=suffix;
    }

    @Parameterized.Parameters
    public static Collection encodeOptions() { // all possible combinations
        return Arrays.asList(new Object[][] {
                { 1, 1, "kot.jpg", "jezozwierz.png", ".png"},
                { 2, 1, "kot.jpg", "jezozwierz.png", ".png"},
                { 4, 1, "kot.jpg", "jezozwierz.png", ".jpg"},
                { 8, 1, "kot.jpg", "jezozwierz.png", ".png"}
        });
    }

    @Test
    public void givenImagesWhenEncodedThenShouldBeTheSameAsControlVersions() throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String baseImg = classLoader.getResource(base).getPath().substring(1);
        String secretImg = classLoader.getResource(secret).getPath().substring(1);
        String encodedImg = classLoader.getResource("encodedOutput" + suffix).getPath().substring(1);
        String decodedImg = classLoader.getResource("decodedOutput" + suffix).getPath().substring(1);

        Steganography.encode(baseImg,secretImg,encodedImg,bitsPetPixel);
        Steganography.decode(encodedImg,decodedImg,bitsPetPixel);

        // take buffer data from botm image files //
        BufferedImage encodedImage= ImageIO.read(new File(decodedImg));
        byte[] actualArray = ((DataBufferByte) encodedImage.getData().getDataBuffer()).getData();

        BufferedImage controlImage = ImageIO.read(new File(secretImg));
        byte[] expectedArray = ((DataBufferByte) controlImage.getData().getDataBuffer()).getData();

        assertArrayEquals(expectedArray, actualArray);
    }
}