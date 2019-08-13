import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Encode {
    private BufferedImage image;
    private byte[] bFile;
    private byte[] newBits;
    private int bitsPerPixel;
    private int mask;
    private int bytesIterator;
    private int offset;
    private boolean endOfSecretNote;

    public Encode(BufferedImage image, String secretFilePath, int bitsPerPixel) throws IOException {
        this.image = image;
        this.bFile = Files.readAllBytes(Paths.get(secretFilePath));
        this.newBits = new byte[3];
        this.bitsPerPixel=bitsPerPixel;
        this.mask = getMask(bitsPerPixel,true);
        this.bytesIterator = 0;
        this.offset = 8-bitsPerPixel;
        this.endOfSecretNote = false;
    }

    public void setOnePixel(int i,int j){
        if(!endOfSecretNote) {
            for (int x = 0; x < 3; x++) { // getting bits to save on one pixel
                if (offset <0) { // next byte
                    bytesIterator++;
                    if (bytesIterator >= bFile.length) { // we reached to end of secret file
                        for (int y = 0; y < newBits.length; y++) // fill the rest bits with 0
                            newBits[y] = 0;
                        endOfSecretNote = true;
                        break;
                    }
                    offset = 8-bitsPerPixel;
                }

                newBits[x] = (byte) ( (bFile[bytesIterator] >> offset) & (~mask));
                offset-=bitsPerPixel;
            }
        }

        int pixel = image.getRGB(i,j);

        //set proper bits
        int blue = (pixel & mask) | (newBits[0]& 0xff); //0xff byte vaule as if it was unsigned 0-255
        int green = ((pixel>>8) & mask) | (newBits[1]& 0xff);

        //convert rgb values to int
        int rgb = ((pixel>>16) & mask) | (newBits[2]& 0xff); // red
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        image.setRGB(i,j,rgb);
    }

    private static int getMask(int bitsPerPixel, boolean reversed){
        int mask;
        switch (bitsPerPixel){
            case 8:
                mask=255;
                break;
            case 4:
                mask=15;
                break;

            case 2:
                mask=3;
                break;

            case 1:default:
                mask=1;
                break;
        }

        if(reversed){
            return 255-mask;
        }

        return mask;
    }
}
