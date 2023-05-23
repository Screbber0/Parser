package screbber;

import screbber.DAO.DefinitionDAO;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String pdfFileName = args[0];

            PDFTextHandler pdfTextHandler = new PDFTextHandler(pdfFileName);
            List<Definition> definitionList = pdfTextHandler.extractDefinitions();

            DefinitionDAO definitionDAO = new DefinitionDAO();
            definitionDAO.insertDefinitionList(definitionList);

            System.out.println("Добавление в базу завершено");
        } else {
            System.out.println("Please provide the PDF file name as a command-line argument.");
        }
    }
}
