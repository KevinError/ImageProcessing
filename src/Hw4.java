public class Hw4 {

    private static List<Integer> runLengthEncode(BufferedImage image) {
        List<Integer> compressedData = new ArrayList<>();
        int width = image.getWidth();
        int height = image.getHeight();

        // Assuming the image is grayscale, get pixel values
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        int count = 1;
        for (int i = 1; i < pixels.length; i++) {
            if (pixels[i] == pixels[i - 1]) {
                count++;
            } else {
                compressedData.add(pixels[i - 1]);  // Pixel value
                compressedData.add(count);          // Run length
                count = 1;
            }
        }

        // Add the last pixel value and its run length
        compressedData.add(pixels[pixels.length - 1]);
        compressedData.add(count);

        return compressedData;
    }
}
