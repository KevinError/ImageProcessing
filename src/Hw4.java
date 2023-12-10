import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;
import java.util.List;

public class Hw4 {
    private static final int BIT_IMAGE_VALUE = 8;

    public static class RLCGrayscaleResultValues {
        ArrayList<Integer> encodedData;
        long encodingTime;
        double compressionRatio;

        public RLCGrayscaleResultValues(ArrayList<Integer> encodedData, long encodingTime, double compressionRatio) {
            this.encodedData = encodedData;
            this.encodingTime = encodingTime;
            this.compressionRatio = compressionRatio;
        }
    }

    public static RLCGrayscaleResultValues RLCGrayscaleEncode(BufferedImage inputImage) {
        long startTime = System.currentTimeMillis();

        ArrayList<Integer> encodedData = new ArrayList<>();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int countNumber;
        int pixelValue;

        for (int y = 0; y < height; y++) {
            countNumber = 1;
            pixelValue = new Color(inputImage.getRGB(0, y)).getRed();

            for (int x = 1; x < width; x++) {
                int pixel = new Color(inputImage.getRGB(x, y)).getRed();
                if (pixel == pixelValue) {
                    countNumber++;
                } else {
                    encodedData.add(pixelValue);
                    encodedData.add(countNumber);
                    pixelValue = pixel;
                    countNumber = 1;
                }
            }

            encodedData.add(pixelValue);
            encodedData.add(countNumber);
        }

        long endTime = System.currentTimeMillis();
        double compressionRatio = ((double) encodedData.size()) / (width * height);

        return new RLCGrayscaleResultValues(encodedData, endTime - startTime, compressionRatio);
    }

    public static BufferedImage RLCGrayScaleDecode(RLCGrayscaleResultValues RLCResult, int width, int height) {
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int x = 0;
        int y = 0;

        for (int i = 0; i < RLCResult.encodedData.size(); i += 2) {
            int value = RLCResult.encodedData.get(i);
            int count = RLCResult.encodedData.get(i + 1);

            for (int j = 0; j < count; j++) {
                outputImage.setRGB(x, y, new Color(value, value, value).getRGB());
                x++;
                if (x >= width) {
                    x = 0;
                    y++;
                }
            }
        }

        return outputImage;
    }

    public static class RLCBitPlaneResultValues {
        List<BitSet> encodedBitPlanes;
        long encodingTime;
        double compressionRatio;

        public RLCBitPlaneResultValues(List<BitSet> encodedBitPlanes, long encodingTime, double compressionRatio) {
            this.encodedBitPlanes = encodedBitPlanes;
            this.encodingTime = encodingTime;
            this.compressionRatio = compressionRatio;
        }
    }

    private static BitSet RLCEncodeBitSet(BitSet bitSet, int size) {
        BitSet encoded = new BitSet();
        boolean lastBit = false;
        int countNumber = 0;
        int encodedIndex = 0;

        for (int i = 0; i < size; i++) {
            boolean currentBit = bitSet.get(i);
            if (currentBit == lastBit) {
                countNumber++;
            } else {
                encodedIndex = CountToBitSet(encoded, countNumber, encodedIndex);
                countNumber = 1; // Reset count for the new bit
                lastBit = currentBit;
            }
        }

        CountToBitSet(encoded, countNumber, encodedIndex);

        return encoded;
    }


    public static RLCBitPlaneResultValues RLCEncodeBitPlanes(BufferedImage inputImage) {
        long startTime = System.currentTimeMillis(); // Start the encoding time measurement

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int imageSize = width * height * BIT_IMAGE_VALUE; // Total number of bits in the original image

        List<BitSet> bitPlanes = new ArrayList<>();
        for (int i = 0; i < BIT_IMAGE_VALUE; i++) {
            bitPlanes.add(new BitSet(width * height));
        }

        for (int bit = 0; bit < BIT_IMAGE_VALUE; bit++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = inputImage.getRGB(x, y) & 0xFF; // Get the grayscale value
                    if (((pixel >> bit) & 1) == 1) {
                        bitPlanes.get(bit).set(y * width + x);
                    }
                }
            }
        }

        List<BitSet> encodedBitPlanes = new ArrayList<>(BIT_IMAGE_VALUE);
        int totalEncodedBits = 0; // To calculate the total number of bits after encoding
        for (BitSet plane : bitPlanes) {
            BitSet encodedPlane = RLCEncodeBitSet(plane, width * height);
            encodedBitPlanes.add(encodedPlane);
            totalEncodedBits += encodedPlane.length();
        }

        double compressionRatio = imageSize / (double) totalEncodedBits;
        long endTime = System.currentTimeMillis(); // End the encoding time measurement
        long encodingTime = endTime - startTime;


        // Return the results in a new BitPlaneRLCResult object
        return new RLCBitPlaneResultValues(encodedBitPlanes, encodingTime, compressionRatio);
    }
    public static BufferedImage RLCDecodeBitPlanes(List<BitSet> encodedBitsetPlanes, int width, int height) {
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[][] pixels = new int[height][width];

        for (int bit = 0; bit < BIT_IMAGE_VALUE; bit++) {
            BitSet encodedPlane = encodedBitsetPlanes.get(bit);
            int bitIndex = 0;
            int pixelIndex = 0;
            boolean currentBit = false;
            while (bitIndex < encodedPlane.length()) {

                int countNumber = countBitSet(encodedPlane, bitIndex);
                bitIndex += Integer.SIZE; // Advance the bit index by the size of an integer

                for (int i = 0; i < countNumber; i++) {
                    int x = pixelIndex % width;
                    int y = pixelIndex / width;
                    pixels[y][x] |= (currentBit ? 1 : 0) << bit;
                    pixelIndex++;
                }
                currentBit = !currentBit; // Flip the bit for the next sequence
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = pixels[y][x];
                int rgb = new Color(value, value, value).getRGB();
                outputImage.setRGB(x, y, rgb);
            }
        }

        return outputImage;
    }

    private static int CountToBitSet(BitSet encodedBitSet, int countNumber, int startIndex) {

        for (int i = 0; i < Integer.SIZE; i++) {
            if ((countNumber & (1 << i)) != 0) {
                encodedBitSet.set(startIndex + i);
            }
        }
        return startIndex + Integer.SIZE;
    }
    private static int countBitSet(BitSet encoded, int startIndex) {
        int countNumber = 0;
        for (int i = 0; i < Integer.SIZE; i++) {
            if (encoded.get(startIndex + i)) {
                countNumber |= 1 << i;
            }
        }
        return countNumber;
    }

    public static class HuffmanNode {
        int data;
        int frequency;
        HuffmanNode left, right;

        HuffmanNode(int data, int frequency) {
            this.data = data;
            this.frequency = frequency;
        }
    }

    static class HuffmanComparator implements Comparator<HuffmanNode> {
        public int compare(HuffmanNode node1, HuffmanNode node2) {
            return node1.frequency - node2.frequency;
        }
    }

    public static HuffmanResultValues HuffmanEncode(int[] imageData, int width, int height) {
        long startTime = System.currentTimeMillis();

        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int key : imageData) {
            frequencyMap.put(key, frequencyMap.getOrDefault(key, 0) + 1);
        }

        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(new HuffmanComparator());
        frequencyMap.forEach((key, value) -> queue.add(new HuffmanNode(key, value)));

        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            HuffmanNode sum = new HuffmanNode(-1, left.frequency + right.frequency);
            sum.left = left;
            sum.right = right;
            queue.add(sum);
        }

        HuffmanNode root = queue.poll();

        // Generate codes
        Map<Integer, String> huffmanCodes = new HashMap<>();
        HuffmanGenerateCode(root, "", huffmanCodes);

        // Encode image
        StringBuilder encodedImage = new StringBuilder();
        for (int key : imageData) {
            encodedImage.append(huffmanCodes.get(key));
        }

        long endTime = System.currentTimeMillis();
        long encodingTime = endTime - startTime;

        // Calculate compression ratio
        int originalSize = imageData.length * Integer.SIZE; // Original size in bits
        int compressedSize = encodedImage.length(); // Compressed size in bits
        double compressionRatio = (double) originalSize / compressedSize;

        return new HuffmanResultValues(encodedImage.toString(), root, width, height, compressionRatio, encodingTime);
    }

    public static BufferedImage HuffmanDecode(String encodedData, HuffmanNode root, int originalLength, int width, int height) {
        int[] outputData = new int[originalLength];
        int index = 0;
        HuffmanNode current = root;
        for (int i = 0; i < encodedData.length(); ) {
            while (current.left != null && current.right != null) {
                current = encodedData.charAt(i) == '0' ? current.left : current.right;
                i++;
            }
            outputData[index++] = current.data;
            current = root;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int grayValue = outputData[y * width + x];
                int rgb = (grayValue << 16) | (grayValue << 8) | grayValue;
                image.setRGB(x, y, rgb);
            }
        }
        return image;
    }

    private static void HuffmanGenerateCode(HuffmanNode node, String code, Map<Integer, String> huffmanCodes) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                huffmanCodes.put(node.data, code);
            }
            HuffmanGenerateCode(node.left, code + "0", huffmanCodes);
            HuffmanGenerateCode(node.right, code + "1", huffmanCodes);
        }
    }

    public static class HuffmanResultValues {
        String encodedData;
        HuffmanNode root;
        int imageWidth;
        int imageHeight;
        double compressionRatio;
        long encodingTime;

        HuffmanResultValues(String encodedData, HuffmanNode root, int width, int height, double compressionRatio, long encodingTime) {
            this.encodedData = encodedData;
            this.root = root;
            this.imageWidth = width;
            this.imageHeight = height;
            this.compressionRatio = compressionRatio;
            this.encodingTime = encodingTime;
        }
    }

    static class LZWCompressionOutcome {
        ArrayList<Integer> codeSequence;
        double ratioCompression;
        double timeEncoding;

        LZWCompressionOutcome(ArrayList<Integer> codeSequence, double ratioCompression, double timeToEncode) {
            this.codeSequence = codeSequence;
            this.ratioCompression = ratioCompression;
            this.timeEncoding = timeToEncode;
        }
    }

    public static LZWCompressionOutcome LZWPerformCompression(BufferedImage img) {
        long start = System.nanoTime();
        byte[] imagePixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        ArrayList<Integer> encodedSequence = LZWencodeData(imagePixels);

        long end = System.nanoTime();
        double compressionRatio = (double) imagePixels.length / encodedSequence.size();
        double encodingDuration = (end - start) / 1e6;

        return new LZWCompressionOutcome(encodedSequence, compressionRatio, encodingDuration);
    }
    private static ArrayList<Integer> LZWencodeData(byte[] pixelData) {
        int nextCode = 256;
        Map<String, Integer> codeMap = new HashMap<>();
        for (int code = 0; code < 256; code++) {
            codeMap.put("" + (char) code, code);
        }

        String currentString = "";
        ArrayList<Integer> outputCodes = new ArrayList<>();
        for (byte pix : pixelData) {
            String combinedString = currentString + (char) (pix & 0xFF);
            if (codeMap.containsKey(combinedString)) {
                currentString = combinedString;
            } else {
                outputCodes.add(codeMap.get(currentString));
                codeMap.put(combinedString, nextCode++);
                currentString = "" + (char) (pix & 0xFF);
            }
        }

        if (!currentString.equals("")) {
            outputCodes.add(codeMap.get(currentString));
        }
        return outputCodes;
    }

    public static BufferedImage LZWPerformDecompression(LZWCompressionOutcome outcome, int imgWidth, int imgHeight, int imageType) {
        byte[] pixelData = LZWdecodeData(outcome.codeSequence);
        BufferedImage reconstructedImg = new BufferedImage(imgWidth, imgHeight, imageType);
        reconstructedImg.getRaster().setDataElements(0, 0, imgWidth, imgHeight, pixelData);
        return reconstructedImg;
    }


    private static byte[] LZWdecodeData(ArrayList<Integer> encodedSequence) {
        int nextCode = 256;
        Map<Integer, String> codeMap = new HashMap<>();
        for (int code = 0; code < 256; code++) {
            codeMap.put(code, "" + (char) code);
        }

        String previousString = "" + (char) (int) encodedSequence.remove(0);
        StringBuilder decodedData = new StringBuilder(previousString);
        for (int nextEncoded : encodedSequence) {
            String stringEntry;
            if (codeMap.containsKey(nextEncoded)) {
                stringEntry = codeMap.get(nextEncoded);
            } else if (nextEncoded == nextCode) {
                stringEntry = previousString + previousString.charAt(0);
            } else {
                throw new IllegalStateException("Invalid encoded value: " + nextEncoded);
            }

            decodedData.append(stringEntry);

            codeMap.put(nextCode++, previousString + stringEntry.charAt(0));
            previousString = stringEntry;
        }

        byte[] byteData = new byte[decodedData.length()];
        for (int idx = 0; idx < decodedData.length(); idx++) {
            byteData[idx] = (byte) decodedData.charAt(idx);
        }
        return byteData;
    }
}
