import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Steganography {

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

    public static void encode(String baseImagePath, String secretFilePath, String resultImagePath,int bitsPerPixel) throws IOException {

        BufferedImage image = ImageIO.read(new File(baseImagePath));
        byte[] bFile = Files.readAllBytes(Paths.get(secretFilePath));
        byte[] newBits = new byte[3];
        int pixel,red,green,blue,bytesIterator=0,offset=8-bitsPerPixel,mask;

        mask = getMask(bitsPerPixel,true); //get mask to work on bits

        boolean endOfSecretNote = false;

        for(int i=0;i<image.getWidth();i++){
            for(int j=0;j<image.getHeight();j++){

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

                        //System.out.println(bFile[bytesIterator] + " : " + newBits[x]);
                        System.out.print((newBits[x] & 0xff) + " ");
                        offset-=bitsPerPixel;
                        //System.out.println(offset);
                    }
                }

                pixel = image.getRGB(i,j);

                //set proper bits
                blue = (pixel & mask) | (newBits[0]& 0xff); //0xff byte vaule as if it was unsigned 0-255
                green = ((pixel>>8) & mask) | (newBits[1]& 0xff);
                red = ((pixel>>16) & mask) | (newBits[2]& 0xff);

                //convert rgb values to int
                int rgb = red;
                rgb = (rgb << 8) + green;
                rgb = (rgb << 8) + blue;
                image.setRGB(i,j,rgb);
            }
        }

        File outputfile = new File(resultImagePath);
        ImageIO.write(image, "png", outputfile);
    }

    private static void setOnePixel (){

    }

    public static void decode(String baseImagePath, String resultFilePath,int bitsPerPixel) throws IOException {
        BufferedImage image = ImageIO.read(new File(baseImagePath));
        int pixel,mask,color;
        byte[] bFile = new byte[3*bitsPerPixel*image.getHeight()*image.getWidth()];
        byte byte1=0;
        int byteLimit=0,pos=0;


        mask = getMask(bitsPerPixel,false); //getting mask that represents amount of bits under which is encoded information

        //reading all pixels from an image
        for(int i=0;i<image.getWidth();i++){
            for(int j=0;j<image.getHeight();j++){
                pixel = image.getRGB(i,j);

                for(int offset=0; offset<=16; offset+=8){ // saving proper amount of bits of each color channel (RGB)
                    color = ((pixel>>offset) & mask);
                    if(byteLimit<8) {
                        byte1 = (byte) ((byte1 << bitsPerPixel) + color);
                        byteLimit+=bitsPerPixel;
                    }else{
                        bFile[pos]=byte1;
                        byte1=(byte)color;
                        byteLimit=bitsPerPixel;
                        pos++;
                    }
                }
            }
        }

        Path path = Paths.get(resultFilePath);
        Files.write(path, bFile); // saving a file
    }
}