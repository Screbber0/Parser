package screbber;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String pdfFileName = args[0];
            PDFTextHandler pdfTextHandler = new PDFTextHandler(pdfFileName);
            //pdfTextHandler.extractItalicText();
            // pdfTextHandler.readInfoFromFile("src/main/resources/full_names.txt");
            pdfTextHandler.extractDefinitions();
        } else {
            System.out.println("Please provide the PDF file name as a command-line argument.");
        }
    }
}
