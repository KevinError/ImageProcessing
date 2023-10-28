import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Hw2 {

    public Hw2(){

    }

    public static BufferedImage equalizeHistogram(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        // Calculate the histogram of the input image
        int[] histogram = new int[256];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel = new Color(inputImage.getRGB(x, y));
                int grayValue = (int) (0.299 * pixel.getRed() + 0.587 * pixel.getGreen() + 0.114 * pixel.getBlue());
                histogram[grayValue]++;
            }
        }

        // Calculate the cumulative distribution function (CDF)
        int[] cdf = new int[256];
        cdf[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + histogram[i];
        }

        // Normalize the CDF
        int totalPixels = width * height;
        for (int i = 0; i < 256; i++) {
            cdf[i] = (int) ((double) cdf[i] / totalPixels * 255);
        }

        // Create the equalized image
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel = new Color(inputImage.getRGB(x, y));
                int grayValue = (int) (0.299 * pixel.getRed() + 0.587 * pixel.getGreen() + 0.114 * pixel.getBlue());
                int newGrayValue = cdf[grayValue];
                Color newPixel = new Color(newGrayValue, newGrayValue, newGrayValue);
                outputImage.setRGB(x, y, newPixel.getRGB());
            }
        }

        return outputImage;
    }

    public static BufferedImage localHistogramEqualization(BufferedImage image, int maskSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage equalizedImage = new BufferedImage(width, height, image.getType());

        // Iterate through each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int halfMaskSize = maskSize / 2;
                int pixelValue = image.getRGB(x, y) & 0xFF;
                int[] histogram = new int[256];

                // Calculate histogram for the local region
                for (int dy = -halfMaskSize; dy <= halfMaskSize; dy++) {
                    for (int dx = -halfMaskSize; dx <= halfMaskSize; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            int localPixelValue = image.getRGB(nx, ny) & 0xFF;
                            histogram[localPixelValue]++;
                        }
                    }
                }

                // Calculate cumulative distribution function (CDF)
                int[] cdf = new int[256];
                cdf[0] = histogram[0];
                for (int i = 1; i < 256; i++) {
                    cdf[i] = cdf[i - 1] + histogram[i];
                }

                // Map pixel value for the center pixel of the local region
                int newPixelValue = (int) (255.0 * cdf[pixelValue] / (maskSize * maskSize));
                equalizedImage.setRGB(x, y, (newPixelValue << 16) | (newPixelValue << 8) | newPixelValue);
            }
        }

        return equalizedImage;
    }

    public static BufferedImage applyAverageSmoothing(BufferedImage image, int mask) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage smoothedImage = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);

        // Iterate through each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] rgbValues = new int[3];
                int totalPixels = 0;
                int halfMaskSize = mask / 2;

                for (int j = -halfMaskSize; j <= halfMaskSize; j++) {
                    for (int i = -halfMaskSize; i <= halfMaskSize; i++) {
                        int newX = x + i;
                        int newY = y + j;

                        // Check if the coordinates are within the image bounds
                        if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                            int pixel = image.getRGB(newX, newY);
                            rgbValues[0] += (pixel >> 16) & 0xFF; // Red
                            rgbValues[1] += (pixel >> 8) & 0xFF;  // Green
                            rgbValues[2] += pixel & 0xFF;         // Blue
                            totalPixels++;
                        }
                    }
                }

                // Calculate the average RGB value for the neighborhood
                int avgRed = rgbValues[0] / totalPixels;
                int avgGreen = rgbValues[1] / totalPixels;
                int avgBlue = rgbValues[2] / totalPixels;

                // Set the smoothed pixel value in the output image
                int smoothedPixel = (avgRed << 16) | (avgGreen << 8) | avgBlue;
                smoothedImage.setRGB(x, y, smoothedPixel);
            }
        }

        return smoothedImage;
    }

    public static BufferedImage applyMedianFilter(BufferedImage image, int maskSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage filteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Iterate through each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] neighborhood = new int[maskSize * maskSize];
                int index = 0;
                int halfMaskSize = maskSize / 2;
                // Iterate through the neighborhood of the pixel
                for (int j = -halfMaskSize; j <= halfMaskSize; j++) {
                    for (int i = -halfMaskSize; i <= halfMaskSize; i++) {
                        int newX = x + i;
                        int newY = y + j;

                        // Check if the coordinates are within the image bounds
                        if (newX >= 0 && newX < width && newY >= 0 && newY < height && index < neighborhood.length) {
                            neighborhood[index] = image.getRGB(newX, newY) & 0xFF;
                            System.out.println(index);
                            index++;
                        }
                    }
                }

                // Sort the neighborhood to find the median
                int median = findMedian(neighborhood);

                // Set the median value as the pixel value in the output image
                int newPixel = (median << 16) | (median << 8) | median;
                filteredImage.setRGB(x, y, newPixel);
            }
        }

        return filteredImage;
    }

    public static int findMedian(int[] array) {
        // Sort the array
        java.util.Arrays.sort(array);

        // Find the median
        int middle = array.length / 2;
        if (array.length % 2 == 1) {
            return array[middle];
        } else {
            return (array[middle - 1] + array[middle]) / 2;
        }
    }

    public static BufferedImage applyLaplacianSharpening(BufferedImage image, int maskSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage sharpenedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Define the Laplacian kernel based on the mask size
        int[][] laplacianKernel = new int[maskSize][maskSize];
        for (int i = 0; i < maskSize; i++) {
            for (int j = 0; j < maskSize; j++) {
                laplacianKernel[i][j] = -1;
            }
        }
        laplacianKernel[maskSize / 2][maskSize / 2] = maskSize * maskSize - 1;

        for (int y = maskSize / 2; y < height - maskSize / 2; y++) {
            for (int x = maskSize / 2; x < width - maskSize / 2; x++) {
                int[] pixel = new int[3];

                // Apply the Laplacian filter to each channel (R, G, B)
                for (int channel = 0; channel < 3; channel++) {
                    int sum = 0;
                    for (int i = -maskSize / 2; i <= maskSize / 2; i++) {
                        for (int j = -maskSize / 2; j <= maskSize / 2; j++) {
                            int rgb = image.getRGB(x + i, y + j);
                            int sample = (rgb >> (channel * 8)) & 0xFF;
                            sum += sample * laplacianKernel[i + maskSize / 2][j + maskSize / 2];
                        }
                    }
                    pixel[channel] = Math.min(255, Math.max(0, pixel[channel] + sum));
                }

                int rgb = (pixel[0] << 16) | (pixel[1] << 8) | pixel[2];
                sharpenedImage.setRGB(x, y, rgb);
            }
        }
        return sharpenedImage;
    }
    public static BufferedImage applyLaplacianFilter(BufferedImage image, int[][] kernel) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage filteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Iterate through each pixel in the image
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;

                // Convolve the kernel with the neighborhood of the pixel
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        int pixel = image.getRGB(x + i, y + j);
                        int red = (pixel >> 16) & 0xFF;
                        int green = (pixel >> 8) & 0xFF;
                        int blue = pixel & 0xFF;

                        sumRed += red * kernel[j + 1][i + 1];
                        sumGreen += green * kernel[j + 1][i + 1];
                        sumBlue += blue * kernel[j + 1][i + 1];
                    }
                }

                // Clamp the values to [0, 255]
                sumRed = Math.min(Math.max(sumRed, 0), 255);
                sumGreen = Math.min(Math.max(sumGreen, 0), 255);
                sumBlue = Math.min(Math.max(sumBlue, 0), 255);

                // Set the Laplacian-filtered pixel value in the output image
                int filteredPixel = (sumRed << 16) | (sumGreen << 8) | sumBlue;
                filteredImage.setRGB(x, y, filteredPixel);
            }
        }

        return filteredImage;
    }

    public static BufferedImage applyHighBoostFilter(BufferedImage image, double boostFactor) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage filteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Define the Laplacian kernel
        int[][] laplacianKernel = {
                {0, -1, 0},
                {-1, 4, -1},
                {0, -1, 0}
        };

        // Apply the Laplacian filter to the input image
        BufferedImage laplacianImage = applyLaplacianFilter(image, laplacianKernel);

        // Iterate through each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int originalPixel = image.getRGB(x, y);
                int laplacianPixel = laplacianImage.getRGB(x, y);

                int originalRed = (originalPixel >> 16) & 0xFF;
                int originalGreen = (originalPixel >> 8) & 0xFF;
                int originalBlue = originalPixel & 0xFF;

                int laplacianRed = (laplacianPixel >> 16) & 0xFF;
                int laplacianGreen = (laplacianPixel >> 8) & 0xFF;
                int laplacianBlue = laplacianPixel & 0xFF;

                // Calculate the boosted pixel values
                int boostedRed = (int) (originalRed + boostFactor * laplacianRed);
                int boostedGreen = (int) (originalGreen + boostFactor * laplacianGreen);
                int boostedBlue = (int) (originalBlue + boostFactor * laplacianBlue);

                // Clamp the values to [0, 255]
                boostedRed = Math.min(Math.max(boostedRed, 0), 255);
                boostedGreen = Math.min(Math.max(boostedGreen, 0), 255);
                boostedBlue = Math.min(Math.max(boostedBlue, 0), 255);

                // Set the boosted pixel value in the output image
                int boostedPixel = (boostedRed << 16) | (boostedGreen << 8) | boostedBlue;
                filteredImage.setRGB(x, y, boostedPixel);
            }
        }

        return filteredImage;
    }

    public static BufferedImage RemoveBitPlane(BufferedImage inputImage, int bitRemoved) {


        ArrayList<Integer> bits = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            bits.add(i);
        }
        bits.remove(bitRemoved - 1);
        // Don't include the bit plane that you removed

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = inputImage.getRGB(x, y) & 0xFF;
                String binaryPixelValue = Integer.toBinaryString(pixelValue);
                while (binaryPixelValue.length() < 8) {
                    binaryPixelValue = "0" + binaryPixelValue;
                }

                int modifiedPixel = 0;
                for (int bit : bits) {
                    int bitPosition = 7 - (bit - 1);

                    // checks the value is '1' to assign the value based on it.
                    if (binaryPixelValue.charAt(bitPosition) == '1') {
                        modifiedPixel += (int) (Math.pow(2, bit - 1));
                        // 2 ^ bit = 2,4,8,16,32,64,128,256
                    }
                }

                Color newColor = new Color(modifiedPixel, modifiedPixel, modifiedPixel);
                outputImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return outputImage;
    }


}
