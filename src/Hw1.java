import java.awt.image.BufferedImage;
import java.io.IOException;

public class Hw1 {

    public Hw1(){

    }

    public static BufferedImage calculateLinear(BufferedImage image, int newSize){
        int width = image.getWidth();
        int height = image.getHeight();
        // Calculate the scaling factors
        double xScale = (double) width / newSize;
        double yScale = (double) height / newSize;

        BufferedImage outputImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_RGB);

        // Iterate over the new image and calculate pixel values
        for (int y = 0; y < newSize; y++) {
            for (int x = 0; x < newSize; x++) {
                int sourceX = (int) (x * xScale);
                int sourceY = (int) (y * yScale);

                int rgb = image.getRGB(sourceX, sourceY);
                outputImage.setRGB(x, y, rgb);
            }
        }
        return outputImage;

    }

    public static BufferedImage calculateBilnear(BufferedImage image, int newSize) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        // Calculate the scaling factors
        double xScale = (double) width / newSize;
        double yScale = (double) height / newSize;

        BufferedImage outputImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_RGB);

        // Iterate over the new image and perform bilinear interpolation
        for (int y = 0; y < newSize; y++) {
            for (int x = 0; x < newSize; x++) {
                double sourceX = x * xScale;
                double sourceY = y * yScale;
                int x0 = (int) sourceX;
                int y0 = (int) sourceY;
                int x1 = Math.min(x0 + 1, image.getWidth() - 1);
                int y1 = Math.min(y0 + 1, image.getHeight() - 1);

                double weightX = sourceX - x0;
                double weightY = sourceY - y0;

                int rgb00 = image.getRGB(x0, y0);
                int rgb01 = image.getRGB(x1, y0);
                int rgb10 = image.getRGB(x0, y1);
                int rgb11 = image.getRGB(x1, y1);

                int red = (int) ((1 - weightX) * (1 - weightY) * ((rgb00 >> 16) & 0xFF)
                        + weightX * (1 - weightY) * ((rgb01 >> 16) & 0xFF)
                        + (1 - weightX) * weightY * ((rgb10 >> 16) & 0xFF)
                        + weightX * weightY * ((rgb11 >> 16) & 0xFF));
                int green = (int) ((1 - weightX) * (1 - weightY) * ((rgb00 >> 8) & 0xFF)
                        + weightX * (1 - weightY) * ((rgb01 >> 8) & 0xFF)
                        + (1 - weightX) * weightY * ((rgb10 >> 8) & 0xFF)
                        + weightX * weightY * ((rgb11 >> 8) & 0xFF));
                int blue = (int) ((1 - weightX) * (1 - weightY) * (rgb00 & 0xFF)
                        + weightX * (1 - weightY) * (rgb01 & 0xFF)
                        + (1 - weightX) * weightY * (rgb10 & 0xFF)
                        + weightX * weightY * (rgb11 & 0xFF));

                int interpolatedRGB = (red << 16) | (green << 8) | blue;
                outputImage.setRGB(x, y, interpolatedRGB);
            }
        }
        return outputImage;

    }

    public static BufferedImage calculateNeighbor(BufferedImage image, int newSize) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage outputImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_RGB);

        // Calculate the scaling factors
        double xScale = (double) width / newSize;
        double yScale = (double) height / newSize;

        // Iterate over the new image and perform nearest-neighbor interpolation
        for (int y = 0; y < newSize; y++) {
            for (int x = 0; x < newSize; x++) {
                int sourceX = (int) (x * xScale);
                int sourceY = (int) (y * yScale);

                int rgb = image.getRGB(sourceX, sourceY);
                outputImage.setRGB(x, y, rgb);
            }
        }
        return outputImage;

    }

    public static BufferedImage varyGrayLevelResolution(BufferedImage image, int bit) {
        int max = 255;
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int L = (int) Math.pow(2, bit);
        int twoBit = max / (L -1);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = image.getRGB(x, y) & 0xFF;
                int newBit = gray / twoBit;
                int scaledBit = newBit * twoBit;

                output.setRGB(x, y, (scaledBit << 16) | (scaledBit << 8) | scaledBit);
            }
        }


        return output;
    }
}
