import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Steganography {

    // Encode Types:
    // 1 - by rows, left to right, start top corner
    // 2 - by rows, left to right, start bottom corner
    // 3 - by rows, right to left, start top corner
    // 4 - by rows, right to left, start bottom corner
    // 5 - by columns, top to bottom, start left corner
    // 6 - by columns, top to bottom, start right corner
    // 7 - by columns, bottom to top, start left corner
    // 8 - by columns, bottom to top, start right corner

    private Steganography(){ }

    public static void encode(String baseImagePath, String secretFilePath, String resultImagePath, int bitsPerPixel, int encodeType) throws IOException {
        BufferedImage image = ImageIO.read(new File(baseImagePath));
        Encode encode = new Encode(image,secretFilePath,bitsPerPixel);

        //encoding all pixels
        encodeDependOnType(encodeType,encode,image);

        File outputfile = new File(resultImagePath);
        ImageIO.write(image, "png", outputfile);
    }

    private static void encodeDependOnType(int encodeType, Encode encode, BufferedImage image){
        switch(encodeType){
            case 1:
                for(int i=0;i<image.getHeight();i++){
                    for(int j=0;j<image.getWidth();j++){
                        encode.setOnePixel(j,i);
                    }
                }
                break;
            case 2:
                for(int i=image.getHeight()-1;i>=0;i--){
                    for(int j=0;j<image.getWidth();j++){
                        encode.setOnePixel(j,i);
                    }
                }
                break;
            case 3:
                for(int i=0;i<image.getHeight();i++){
                    for(int j=image.getWidth()-1;j>=0;j--){
                        encode.setOnePixel(j,i);
                    }
                }
                break;
            case 4:
                for(int i=image.getHeight()-1;i>=0;i--){
                    for(int j=image.getWidth()-1;j>=0;j--){
                        encode.setOnePixel(j,i);
                    }
                }
                break;
            case 5:
                for(int i=0;i<image.getWidth();i++){
                    for(int j=0;j<image.getHeight();j++){
                        encode.setOnePixel(i,j);
                    }
                }
                break;
            case 6:
                for(int i=image.getWidth()-1;i>=0;i--){
                    for(int j=0;j<image.getHeight();j++){
                        encode.setOnePixel(i,j);
                    }
                }
                break;
            case 7:
                for(int i=0;i<image.getWidth();i++){
                    for(int j=image.getHeight()-1;j>=0;j--){
                        encode.setOnePixel(i,j);
                    }
                }
                break;
            case 8:
                for(int i=image.getWidth()-1;i>=0;i--){
                    for(int j=image.getHeight()-1;j>=0;j--){
                        encode.setOnePixel(i,j);
                    }
                }
                break;
        }
    }

    public static void decode(String baseImagePath, String resultFilePath,int bitsPerPixel,int encodeType) throws IOException {
        BufferedImage image = ImageIO.read(new File(baseImagePath));
        Decode decode = new Decode(image,bitsPerPixel);

        //reading all pixels from an image
        decodeDependOnType(encodeType,decode,image);

        byte[] result = decode.cutFileSize();

        Path path = Paths.get(resultFilePath);
        Files.write(path, result); // saving a file
    }

    private static void decodeDependOnType(int encodeType, Decode decode, BufferedImage image){
        switch(encodeType){
            case 1:
                for(int i=0;i<image.getHeight();i++){
                    for(int j=0;j<image.getWidth();j++){
                        decode.readOnePixel(j,i);
                    }
                }
                break;
            case 2:
                for(int i=image.getHeight()-1;i>=0;i--){
                    for(int j=0;j<image.getWidth();j++){
                        decode.readOnePixel(j,i);
                    }
                }
                break;
            case 3:
                for(int i=0;i<image.getHeight();i++){
                    for(int j=image.getWidth()-1;j>=0;j--){
                        decode.readOnePixel(j,i);
                    }
                }
                break;
            case 4:
                for(int i=image.getHeight()-1;i>=0;i--){
                    for(int j=image.getWidth()-1;j>=0;j--){
                        decode.readOnePixel(j,i);
                    }
                }
                break;
            case 5:
                for(int i=0;i<image.getWidth();i++){
                    for(int j=0;j<image.getHeight();j++){
                        decode.readOnePixel(i,j);
                    }
                }
                break;
            case 6:
                for(int i=image.getWidth()-1;i>=0;i--){
                    for(int j=0;j<image.getHeight();j++){
                        decode.readOnePixel(i,j);
                    }
                }
                break;
            case 7:
                for(int i=0;i<image.getWidth();i++){
                    for(int j=image.getHeight()-1;j>=0;j--){
                        decode.readOnePixel(i,j);
                    }
                }
                break;
            case 8:
                for(int i=image.getWidth()-1;i>=0;i--){
                    for(int j=image.getHeight()-1;j>=0;j--){
                        decode.readOnePixel(i,j);
                    }
                }
                break;
        }
    }
}