import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Hw4Main {
    static final String imageSource = "C:\\Users\\khk40\\Documents\\GitHub\\ImageProcessing\\resource\\Lena.jpg";

    public static int[] findGrayScale(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] grayscale = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                int gray = (rgb & 0xFF);
                grayscale[y * width + x] = gray;
            }
        }
        return grayscale;
    }

    public static double CalculateRootMeanSquareError(BufferedImage input, BufferedImage compressed) {
        double mse = 0;
        int height = input.getHeight();
        int width = input.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int originalPixel = new Color(input.getRGB(x, y)).getRed();
                int compressedPixel = new Color(compressed.getRGB(x, y)).getRed();
                mse += Math.pow(originalPixel - compressedPixel, 2);
            }
        }

        mse /= (width * height);
        return Math.sqrt(mse);
    }

    public static void main(String[] args) throws IOException {
        BufferedImage inputImage = ImageIO.read(new File(imageSource));

        Hw4.RLCGrayscaleResultValues RLCvalues = Hw4.RLCGrayscaleEncode(inputImage);
        BufferedImage RLCoutput = Hw4.RLCGrayScaleDecode(RLCvalues, inputImage.getWidth(), inputImage.getHeight());
        System.out.println("Compression Ratio: " + RLCvalues.compressionRatio);
        System.out.println("Encoding Time: " + RLCvalues.encodingTime);
        System.out.println("Root Mean Square Error:" + CalculateRootMeanSquareError(inputImage, RLCoutput));

        Hw4.RLCBitPlaneResultValues RLCBitPlaneValues = Hw4.RLCEncodeBitPlanes(inputImage);
        BufferedImage RLCBitPlaneOutput = Hw4.RLCDecodeBitPlanes(RLCBitPlaneValues.encodedBitPlanes, inputImage.getWidth(), inputImage.getHeight());
        System.out.println("Compression Ratio: " + RLCBitPlaneValues.compressionRatio);
        System.out.println("Encoding Time: " + RLCBitPlaneValues.encodingTime);
        System.out.println("Root Mean Square Error:" + CalculateRootMeanSquareError(inputImage, RLCBitPlaneOutput));

        int[] pixels = findGrayScale(inputImage);
        Hw4.HuffmanResultValues HuffmanresultValues = Hw4.HuffmanEncode(pixels, inputImage.getWidth(), inputImage.getHeight());
        BufferedImage HuffmanOutputImage = Hw4.HuffmanDecode(HuffmanresultValues.encodedData, HuffmanresultValues.root, pixels.length, HuffmanresultValues.imageWidth, HuffmanresultValues.imageHeight);
        System.out.println("Compression Ratio: " + HuffmanresultValues.compressionRatio);
        System.out.println("Encoding Time: " + HuffmanresultValues.encodingTime);
        System.out.println("Root Mean Square Error:" + CalculateRootMeanSquareError(inputImage, HuffmanOutputImage));


        Hw4.LZWCompressionOutcome LZWcompressed =Hw4.LZWPerformCompression(inputImage);
        System.out.println("Compression Ratio: " + LZWcompressed.ratioCompression);
        System.out.println("Encoding Time: " + LZWcompressed.timeEncoding + "ms");
        BufferedImage LZWOutputImage = Hw4.LZWPerformDecompression(LZWcompressed, inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
        System.out.println("Root Mean Square Error:" + CalculateRootMeanSquareError(inputImage, LZWOutputImage));

        ImageIO.write(HuffmanOutputImage, "jpg", new File("C:\\Users\\khk40\\Documents\\GitHub\\ImageProcessing\\resource\\RLCOutput.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
