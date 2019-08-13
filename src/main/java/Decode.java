import java.awt.image.BufferedImage;

public class Decode {
    private BufferedImage image;
    private byte[] bFile;
    private byte byte1;
    private int byteLimit;
    private int pos;
    private int mask;
    private int bitsPerPixel;

    public Decode(BufferedImage image, int bitsPerPixel){
        this.image = image;
        this.bitsPerPixel = bitsPerPixel;
        this.bFile = new byte[3*bitsPerPixel*image.getHeight()*image.getWidth()];
        this.byteLimit = 0;
        this.pos = 0;
        this.mask = getMask(bitsPerPixel,false); //getting mask that represents amount of bits under which is encoded information
    }

    public void readOnePixel(int i,int j){
        int pixel = image.getRGB(i,j);

        for(int offset=0; offset<=16; offset+=8){ // saving proper amount of bits of each color channel (RGB)
            int color = ((pixel>>offset) & mask);
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

    public byte[] cutFileSize(){
        //find index of last normal value (remove all 0 in the end of array)
        int index=0;
        for(int i = bFile.length-1;i>=0;i--){
            if(bFile[i]!=0){
                index=i+1;
                break;
            }
        }
        byte[] result = new byte[index];
        if (index + 1 >= 0) System.arraycopy(bFile, 0, result, 0, index); // copy

        return result;
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
