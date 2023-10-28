import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.IOException;
import javax.swing.JFrame;


public class Main{


    static final String imageSource = "C:\\Users\\khk40\\Documents\\GitHub\\ImageProcessing\\resource\\Lena.jpg";
    static final String outputSource = "C:\\Users\\khk40\\Documents\\GitHub\\ImageProcessing\\resource\\";
    private JFrame frame;
    private static JLabel imageLabel;

    // frame
    static JFrame f;

    // label

    // combobox
    static JComboBox c1;
    static JTextField c2;
    static JComboBox c3;

    static ImageIcon icon2;
    static JLabel imageLabel2;




    private static void chooseMethod() throws IOException {
        // Load and display an image (change the path as needed)
        String selectedMethodName = (String) c1.getSelectedItem();
        String selectedResolutionString = c2.getText();
        String imgResource = imageSource;

        int selectedResolution = Integer.valueOf( selectedResolutionString);
        int selectedMethodName2 = Integer.valueOf((String) c3.getSelectedItem());
        ImageIcon imageicon =  new ImageIcon(outputSource + "output.jpg");

        try {
            // Load the input image
            BufferedImage inputImage = ImageIO.read(new File(imgResource));
            BufferedImage outputImage = inputImage;

            // Perform histogram equalization
            if (selectedMethodName.equals("global histogram equalization")) {
                outputImage = Hw2.equalizeHistogram(inputImage);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "global.jpg"));
                imageicon =  new ImageIcon(outputSource + "global.jpg");
                System.out.println("global");
            } else if (selectedMethodName.equals("local histogram equalization")) {
                outputImage = Hw2.localHistogramEqualization(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "local.jpg"));
                imageicon =  new ImageIcon(outputSource + "local.jpg");
                System.out.println("global2");
            } else if (selectedMethodName.equals("Smoothing filter")) {
                outputImage = Hw2.applyAverageSmoothing(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "smoothing.jpg"));
                imageicon =  new ImageIcon(outputSource + "smoothing.jpg");
            } else if (selectedMethodName.equals("Median filter")) {
                outputImage = Hw2.applyMedianFilter(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "median.jpg"));
                imageicon =  new ImageIcon(outputSource + "median.jpg");
            } else if (selectedMethodName.equals("Sharpening Laplacian filter")) {
                outputImage = Hw2.applyLaplacianSharpening(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "laplacian.jpg"));
                imageicon =  new ImageIcon(outputSource + "laplacian.jpg");
            } else if (selectedMethodName.equals("High-boosting filter")) {
                outputImage = Hw2.applyHighBoostFilter(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "highboost.jpg"));
                imageicon =  new ImageIcon(outputSource + "highboost.jpg");
            } else if (selectedMethodName.equals("removeBitPlane")) {
                outputImage = Hw2.RemoveBitPlane(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "removeBitPlane.jpg"));
                imageicon =  new ImageIcon(outputSource + "removeBitPlane.jpg");
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            } else if (selectedMethodName.equals("nearest neighbor method")) {
                outputImage = Hw1.calculateNeighbor(inputImage, selectedResolution);
                ImageIO.write(outputImage, "jpg", new File( outputSource+ "neighbor.jpg"));
                imageicon =  new ImageIcon( outputSource + "neighbor.jpg");
            } else if (selectedMethodName.equals("linear method")) {
                outputImage = Hw1.calculateLinear(inputImage, selectedResolution);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "Linear.jpg"));
                imageicon =  new ImageIcon( outputSource + "Linear.jpg");
            } else if (selectedMethodName.equals("bilinear interpolation method")) {
                outputImage = Hw1.calculateBilnear(inputImage, selectedResolution);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "Bilinear.jpg"));
                imageicon =  new ImageIcon(outputSource + "Bilinear.jpg");
            } else if (selectedMethodName.equals("gray level resolution")) {
                outputImage = Hw1.varyGrayLevelResolution(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "grayLevelResolution.jpg"));
                imageicon =  new ImageIcon(outputSource + "grayLevelResolution.jpg");
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            } else if (selectedMethodName.equals("arithmetic")) {
                outputImage = Hw3.applyArithmeticMeanFilter(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "arithmetic.jpg"));
                imageicon =  new ImageIcon(outputSource + "arithmetic.jpg");
            } else if (selectedMethodName.equals("geometric")) {
                outputImage = Hw3.applyGeometricMeanFilter(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "geometric.jpg"));
                imageicon =  new ImageIcon(outputSource + "geometric.jpg");
            } else if (selectedMethodName.equals("harmonic")) {
                outputImage = Hw3.applyHarmonicMeanFilter(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "harmonic.jpg"));
                imageicon =  new ImageIcon(outputSource + "harmonic.jpg");
            } else if (selectedMethodName.equals("contraharmonic")) {
                outputImage = Hw3.applyContraharmonicMeanFilter(inputImage, selectedMethodName2, selectedResolution);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "contraharmonic.jpg"));
                imageicon =  new ImageIcon(outputSource + "contraharmonic.jpg");
            } else if (selectedMethodName.equals("max filter")) {
                outputImage = Hw3.applyMaxFilter(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "max filter.jpg"));
                imageicon =  new ImageIcon(outputSource + "max filter.jpg");
            } else if (selectedMethodName.equals("min filter")) {
                outputImage = Hw3.applyMinFilter(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "min filter.jpg"));
                imageicon =  new ImageIcon(outputSource + "min filter.jpg");
            } else if (selectedMethodName.equals("midpoint filter")) {
                outputImage = Hw3.applyMidpointFilter(inputImage, selectedMethodName2);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "midpoint filter.jpg"));
                imageicon =  new ImageIcon(outputSource + "midpoint filter.jpg");
            } else if (selectedMethodName.equals("alpha-trimmed")) {
                outputImage = Hw3.applyAlphaTrimmedMeanFilter(inputImage, selectedMethodName2, selectedResolution);
                ImageIO.write(outputImage, "jpg", new File(outputSource + "alpha-trimmed.jpg"));
                imageicon =  new ImageIcon(outputSource + "alpha-trimmed.jpg");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        icon2.setImage(imageicon.getImage());
        imageLabel2.setIcon(icon2);

        imageLabel2.repaint();




    }

    public static void Display2(){
        JFrame frame = new JFrame("Multiple Dropdowns Example");
        ImageIcon icon = new ImageIcon(imageSource);
        icon2 = new ImageIcon(outputSource + "output.jpg");
        JLabel imageLabel1 = new JLabel();
        imageLabel2 = new JLabel();

        imageLabel1.setIcon(icon);
        imageLabel2.setIcon(icon2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);

        // Create the first JComboBox (Dropdown menu)
        String[] options1 = {
                "nearest neighbor method", "linear method", "bilinear interpolation method", "gray level resolution",
                "global histogram equalization", "local histogram equalization",
                "Smoothing filter", "Median filter", "Sharpening Laplacian filter", "High-boosting filter",
                "removeBitPlane", "arithmetic", "geometric", "harmonic",
                "contraharmonic", "max filter", "min filter", "midpoint filter", "alpha-trimmed"};
        c1= new JComboBox<>(options1);
        c1.setSelectedIndex(0); // Set the default selected item

        c2 = new JTextField(20);
        c2.setText("3");



        String[] options3 = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
        c3 = new JComboBox<>(options3);
        c3.setSelectedIndex(0); // Set the default selected item

        // Create a JLabel to display the selected item for the first dropdown
        JLabel label1 = new JLabel("Resolution or order: ");

        // Create a JLabel to display the selected item for the second dropdown
        JLabel label2 = new JLabel("Selected Item 2: ");

        // Create a JLabel to display the selected item for the second dropdown
        JLabel label3 = new JLabel("Selected Item 3: ");

        // Create a button to get the selected item from the first dropdown
        JButton button1 = new JButton("Choose the method");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = c1.getSelectedItem().toString();
                label1.setText("Selected method: " + selectedItem + ", Resolution or order: ");

                String selectedItem2 = c2.getText();
                label2.setText("Selected Resolution: " + selectedItem2);

                String selectedItem3 = c3.getSelectedItem().toString();
                label3.setText("Choose the mask size: " + selectedItem3);

                try {
                    chooseMethod();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        // Create a JPanel to hold components
        JPanel panel = new JPanel();
        panel.add(c1, BorderLayout.WEST);
        panel.add(button1, BorderLayout.WEST);
        panel.add(label1, BorderLayout.WEST);
        panel.add(c2, BorderLayout.WEST);

        panel.add(c3,  BorderLayout.WEST);

        //panel = new JPanel(new GridLayout(1, 2));
        panel.add(imageLabel1,  BorderLayout.EAST);
        panel.add(imageLabel2, BorderLayout.EAST);


        // Add the panel to the frame
        frame.add(panel);

        // Display the frame
        frame.setVisible(true);
    }



    public static void main(String[] args) throws IOException {
        //String imgResource2 = "C:\\Users\\khk40\\Documents\\GitHub\\hw5800final\\ImageProcessing\\resource\\output.jpg";
        Display2();

//        try {
//            // Load the input image
//            BufferedImage inputImage = ImageIO.read(new File(imageSource));
//
//            // Perform histogram equalization
//            BufferedImage outputImage = Hw2.combineBitPlanes(inputImage, new int[]{1, 2, 3, 4, 5,6,8});
//
//            // Save the result
//            ImageIO.write(outputImage, "jpg", new File("C:\\Users\\khk40\\Documents\\GitHub\\hw5800final\\ImageProcessing\\resource\\answer2.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}