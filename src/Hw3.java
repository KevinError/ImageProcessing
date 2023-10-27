import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Hw3 {

    public Hw3() {

    }

    public static BufferedImage applyArithmeticMeanFilter(BufferedImage image, int resolution) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int halfResolution = resolution / 2;

        for (int y = halfResolution; y < height - halfResolution; y++) {
            for (int x = halfResolution; x < width - halfResolution; x++) {
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;

                for (int offsetY = -halfResolution; offsetY <= halfResolution; offsetY++) {
                    for (int offsetX = -halfResolution; offsetX <= halfResolution; offsetX++) {
                        int rgb = image.getRGB(x + offsetX, y + offsetY);
                        sumRed += (rgb >> 16) & 0xFF;
                        sumGreen += (rgb >> 8) & 0xFF;
                        sumBlue += rgb & 0xFF;
                    }
                }

                int numPixels = resolution * resolution;
                int avgRed = sumRed / numPixels;
                int avgGreen = sumGreen / numPixels;
                int avgBlue = sumBlue / numPixels;

                int newRGB = (avgRed << 16) | (avgGreen << 8) | avgBlue;
                output.setRGB(x, y, newRGB);
            }
        }

        return output;

    }

    public static BufferedImage applyGeometricMeanFilter(BufferedImage originalImage, int resolution) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int halfResolution = resolution / 2;

        for (int y = halfResolution; y < height - halfResolution; y++) {
            for (int x = halfResolution; x < width - halfResolution; x++) {
                double productRed = 1.0;
                double productGreen = 1.0;
                double productBlue = 1.0;

                for (int offsetY = -halfResolution; offsetY <= halfResolution; offsetY++) {
                    for (int offsetX = -halfResolution; offsetX <= halfResolution; offsetX++) {
                        int rgb = originalImage.getRGB(x + offsetX, y + offsetY);
                        productRed *= (rgb >> 16) & 0xFF;
                        productGreen *= (rgb >> 8) & 0xFF;
                        productBlue *= rgb & 0xFF;
                    }
                }

                int numPixels = resolution * resolution;
                int geoMeanRed = (int) Math.pow(productRed, 1.0 / numPixels);
                int geoMeanGreen = (int) Math.pow(productGreen, 1.0 / numPixels);
                int geoMeanBlue = (int) Math.pow(productBlue, 1.0 / numPixels);

                int newRGB = (geoMeanRed << 16) | (geoMeanGreen << 8) | geoMeanBlue;
                output.setRGB(x, y, newRGB);
            }
        }

        return output;
    }

    public static BufferedImage applyHarmonicMeanFilter(BufferedImage originalImage, int resolution) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int halfResolution = resolution / 2;

        for (int y = halfResolution; y < height - halfResolution; y++) {
            for (int x = halfResolution; x < width - halfResolution; x++) {
                double sumReciprocalRed = 0.0;
                double sumReciprocalGreen = 0.0;
                double sumReciprocalBlue = 0.0;

                for (int offsetY = -halfResolution; offsetY <= halfResolution; offsetY++) {
                    for (int offsetX = -halfResolution; offsetX <= halfResolution; offsetX++) {
                        int rgb = originalImage.getRGB(x + offsetX, y + offsetY);
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        // Checks zero division
                        if (red != 0) {
                            sumReciprocalRed += 1.0 / red;
                        }
                        if (green != 0) {
                            sumReciprocalGreen += 1.0 / green;
                        }
                        if (blue != 0) {
                            sumReciprocalBlue += 1.0 / blue;
                        }
                    }
                }

                int numPixels = resolution * resolution;
                int harmonicMeanRed = (int) (numPixels / sumReciprocalRed);
                int harmonicMeanGreen = (int) (numPixels / sumReciprocalGreen);
                int harmonicMeanBlue = (int) (numPixels / sumReciprocalBlue);

                int newRGB = (harmonicMeanRed << 16) | (harmonicMeanGreen << 8) | harmonicMeanBlue;
                output.setRGB(x, y, newRGB);
            }
        }

        return output;
    }

    public static BufferedImage applyContraharmonicMeanFilter(BufferedImage originalImage, int resolution, double order) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int halfResolution = resolution / 2;

        for (int y = halfResolution; y < height - halfResolution; y++) {
            for (int x = halfResolution; x < width - halfResolution; x++) {
                double numeratorRed = 0.0;
                double numeratorGreen = 0.0;
                double numeratorBlue = 0.0;

                double denominatorRed = 0.0;
                double denominatorGreen = 0.0;
                double denominatorBlue = 0.0;

                for (int offsetY = -halfResolution; offsetY <= halfResolution; offsetY++) {
                    for (int offsetX = -halfResolution; offsetX <= halfResolution; offsetX++) {
                        int rgb = originalImage.getRGB(x + offsetX, y + offsetY);
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        numeratorRed += Math.pow(red, order + 1);
                        numeratorGreen += Math.pow(green, order + 1);
                        numeratorBlue += Math.pow(blue, order + 1);

                        denominatorRed += Math.pow(red, order);
                        denominatorGreen += Math.pow(green, order);
                        denominatorBlue += Math.pow(blue, order);
                    }
                }

                int contraharmonicMeanRed = (int) (numeratorRed / denominatorRed);
                int contraharmonicMeanGreen = (int) (numeratorGreen / denominatorGreen);
                int contraharmonicMeanBlue = (int) (numeratorBlue / denominatorBlue);

                int newRGB = (contraharmonicMeanRed << 16) | (contraharmonicMeanGreen << 8) | contraharmonicMeanBlue;
                output.setRGB(x, y, newRGB);
            }
        }

        return output;
    }

    public static BufferedImage applyMaxFilter(BufferedImage originalImage, int filterSize) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int halfFilterSize = filterSize / 2;

        for (int y = halfFilterSize; y < height - halfFilterSize; y++) {
            for (int x = halfFilterSize; x < width - halfFilterSize; x++) {
                int maxRed = 0;
                int maxGreen = 0;
                int maxBlue = 0;

                for (int offsetY = -halfFilterSize; offsetY <= halfFilterSize; offsetY++) {
                    for (int offsetX = -halfFilterSize; offsetX <= halfFilterSize; offsetX++) {
                        int rgb = originalImage.getRGB(x + offsetX, y + offsetY);
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        maxRed = Math.max(maxRed, red);
                        maxGreen = Math.max(maxGreen, green);
                        maxBlue = Math.max(maxBlue, blue);
                    }
                }

                int newRGB = (maxRed << 16) | (maxGreen << 8) | maxBlue;
                output.setRGB(x, y, newRGB);
            }
        }

        return output;
    }

    public static BufferedImage applyMinFilter(BufferedImage originalImage, int filterSize) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int halfFilterSize = filterSize / 2;

        for (int y = halfFilterSize; y < height - halfFilterSize; y++) {
            for (int x = halfFilterSize; x < width - halfFilterSize; x++) {
                int minRed = 255;
                int minGreen = 255;
                int minBlue = 255;

                for (int offsetY = -halfFilterSize; offsetY <= halfFilterSize; offsetY++) {
                    for (int offsetX = -halfFilterSize; offsetX <= halfFilterSize; offsetX++) {
                        int rgb = originalImage.getRGB(x + offsetX, y + offsetY);
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        minRed = Math.min(minRed, red);
                        minGreen = Math.min(minGreen, green);
                        minBlue = Math.min(minBlue, blue);
                    }
                }

                int newRGB = (minRed << 16) | (minGreen << 8) | minBlue;
                output.setRGB(x, y, newRGB);
            }
        }

        return output;
    }

    public static BufferedImage applyMidpointFilter(BufferedImage originalImage, int filterSize) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int halfFilterSize = filterSize / 2;
        int numPixels = filterSize * filterSize;
        int[] redValues = new int[numPixels];
        int[] greenValues = new int[numPixels];
        int[] blueValues = new int[numPixels];

        for (int y = halfFilterSize; y < height - halfFilterSize; y++) {
            for (int x = halfFilterSize; x < width - halfFilterSize; x++) {
                int index = 0;

                for (int offsetY = -halfFilterSize; offsetY <= halfFilterSize; offsetY++) {
                    for (int offsetX = -halfFilterSize; offsetX <= halfFilterSize; offsetX++) {
                        int rgb = originalImage.getRGB(x + offsetX, y + offsetY);
                        redValues[index] = (rgb >> 16) & 0xFF;
                        greenValues[index] = (rgb >> 8) & 0xFF;
                        blueValues[index] = rgb & 0xFF;
                        index++;
                    }
                }

                Arrays.sort(redValues);
                Arrays.sort(greenValues);
                Arrays.sort(blueValues);

                int midpointRed = redValues[numPixels / 2];
                int midpointGreen = greenValues[numPixels / 2];
                int midpointBlue = blueValues[numPixels / 2];

                int newRGB = (midpointRed << 16) | (midpointGreen << 8) | midpointBlue;
                output.setRGB(x, y, newRGB);
            }
        }

        return output;
    }

    public static BufferedImage applyAlphaTrimmedMeanFilter(BufferedImage originalImage, int filterSize, int alpha) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int halfFilterSize = filterSize / 2;

        for (int y = halfFilterSize; y < height - halfFilterSize; y++) {
            for (int x = halfFilterSize; x < width - halfFilterSize; x++) {
                int[] redValues = new int[filterSize * filterSize];
                int[] greenValues = new int[filterSize * filterSize];
                int[] blueValues = new int[filterSize * filterSize];

                int index = 0;

                for (int offsetY = -halfFilterSize; offsetY <= halfFilterSize; offsetY++) {
                    for (int offsetX = -halfFilterSize; offsetX <= halfFilterSize; offsetX++) {
                        int rgb = originalImage.getRGB(x + offsetX, y + offsetY);
                        redValues[index] = (rgb >> 16) & 0xFF;
                        greenValues[index] = (rgb >> 8) & 0xFF;
                        blueValues[index] = rgb & 0xFF;
                        index++;
                    }
                }

                // Sort the pixel values for each channel
                sortChannelValues(redValues);
                sortChannelValues(greenValues);
                sortChannelValues(blueValues);

                // Compute the alpha-trimmed mean for each channel
                int numPixels = filterSize * filterSize;
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;

                for (int i = alpha / 2; i < numPixels - alpha / 2; i++) {
                    sumRed += redValues[i];
                    sumGreen += greenValues[i];
                    sumBlue += blueValues[i];
                }

                int trimmedMeanRed = sumRed / (numPixels - alpha);
                int trimmedMeanGreen = sumGreen / (numPixels - alpha);
                int trimmedMeanBlue = sumBlue / (numPixels - alpha);

                int newRGB = (trimmedMeanRed << 16) | (trimmedMeanGreen << 8) | trimmedMeanBlue;
                output.setRGB(x, y, newRGB);
            }
        }

        return output;
    }

    public static void sortChannelValues(int[] values) {
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (values[i] > values[j]) {
                    int temp = values[i];
                    values[i] = values[j];
                    values[j] = temp;
                }
            }
        }
    }

}
