import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFileProcessor {

    public static void main(String[] args) {
        String inputFile = "products.csv";
        String outputFile = "expensive_products.csv";
        double priceThreshold = 1000.0;

        try {
            // Step 1: Create sample products.csv file
            createSampleCSV(inputFile);
            System.out.println("✓ Sample CSV file created: " + inputFile);

            // Step 2-5: Read, parse, filter and write to new file
            List<String> expensiveProducts = processCSVFile(inputFile, priceThreshold);
            writeFilteredData(outputFile, expensiveProducts);

            // Step 6: Print success message and verify new file
            System.out.println("✓ Processing completed successfully!");
            System.out.println("✓ Expensive products (price > $" + priceThreshold + ") saved to: " + outputFile);

            // Verify and display contents of new file
            verifyOutputFile(outputFile);

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Step 1: Create a sample products.csv file with name and price
     */
    private static void createSampleCSV(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Product Name,Price\n");
            writer.write("Laptop,1299.99\n");
            writer.write("Mouse,25.50\n");
            writer.write("Keyboard,89.99\n");
            writer.write("Monitor,599.00\n");
            writer.write("Graphics Card,1599.99\n");
            writer.write("USB Cable,12.99\n");
            writer.write("Gaming Chair,899.99\n");
            writer.write("Smartphone,1199.00\n");
            writer.write("Tablet,399.99\n");
            writer.write("Headphones,249.99\n");
            writer.write("Webcam,79.99\n");
            writer.write("Server,2499.99\n");
        }
    }

    /**
     * Steps 2-4: Read CSV file, parse data, and filter products
     */
    private static List<String> processCSVFile(String inputFile, double threshold) throws IOException {
        List<String> expensiveProducts = new ArrayList<>();

        // Step 2: Use BufferedReader to read lines from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            boolean isFirstLine = true;

            System.out.println("\n--- Processing Products ---");

            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    expensiveProducts.add(line); // Keep header for output file
                    isFirstLine = false;
                    continue;
                }

                // Step 3: Split each line by comma to get name and price
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String productName = parts[0].trim();
                    String priceString = parts[1].trim();

                    try {
                        // Step 4: Convert price to double and check if > threshold
                        double price = Double.parseDouble(priceString);

                        System.out.printf("Processing: %-15s - $%.2f", productName, price);

                        if (price > threshold) {
                            expensiveProducts.add(line);
                            System.out.println(" ✓ [EXPENSIVE]");
                        } else {
                            System.out.println(" [Below threshold]");
                        }

                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid price format for product: " + productName);
                    }
                }
            }
        }

        return expensiveProducts;
    }

    /**
     * Step 5: Use FileWriter to write matching products to a new CSV file
     */
    private static void writeFilteredData(String outputFile, List<String> products) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            for (String product : products) {
                writer.write(product + "\n");
            }
        }

        System.out.println("\n✓ Filtered data written to: " + outputFile);
        System.out.println("✓ Total expensive products found: " + (products.size() - 1)); // -1 for header
    }

    /**
     * Step 6: Verify new file contents
     */
    private static void verifyOutputFile(String filename) throws IOException {
        System.out.println("\n--- Contents of " + filename + " ---");

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                System.out.println(lineNumber + ". " + line);
                lineNumber++;
            }
        }

        // Check file size
        File file = new File(filename);
        System.out.println("\n✓ File size: " + file.length() + " bytes");
        System.out.println("✓ File exists: " + file.exists());
    }
}

// Alternative approach using more advanced Java features
class AdvancedCSVProcessor {

    public static void processWithStreams(String inputFile, String outputFile, double threshold) {
        try {
            // Create sample file
            createSampleData(inputFile);

            // Process using Java 8 Streams and try-with-resources
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    FileWriter writer = new FileWriter(outputFile)) {

                // Read all lines, skip header, filter and collect
                List<String> expensiveProducts = reader.lines()
                        .skip(1) // Skip header
                        .filter(line -> {
                            String[] parts = line.split(",");
                            if (parts.length >= 2) {
                                try {
                                    double price = Double.parseDouble(parts[1].trim());
                                    return price > threshold;
                                } catch (NumberFormatException e) {
                                    return false;
                                }
                            }
                            return false;
                        })
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                // Write header first
                writer.write("Product Name,Price\n");

                // Write filtered products
                for (String product : expensiveProducts) {
                    writer.write(product + "\n");
                }

                System.out.println("Advanced processing completed!");
                System.out.println("Found " + expensiveProducts.size() + " expensive products");
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void createSampleData(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Product Name,Price\n");
            writer.write("Premium Laptop,1599.99\n");
            writer.write("Basic Mouse,15.99\n");
            writer.write("Mechanical Keyboard,159.99\n");
            writer.write("4K Monitor,1299.00\n");
        }
    }
}