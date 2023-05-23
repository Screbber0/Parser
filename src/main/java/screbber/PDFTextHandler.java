package screbber;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import screbber.strategy.ExtractNamesOfDefinitionStrategy;
import screbber.strategy.ExtractInfoOfDefinitionStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDFTextHandler {
    private static final String SEPARATOR = "|-------------------------------------------|";
    private static final String OUTPUT_DIRECTORY = "src/main/resources/";

    private final String filePath;

    public PDFTextHandler(String filePath) {
        this.filePath = filePath;
    }

    private void addToHashMap(Map<Integer, List<String>> map, int position, String text) {
        if (map.containsKey(position)) {
            map.get(position).add(text);
        } else {
            List<String> sentenceList = new ArrayList<>();
            sentenceList.add(text);
            map.put(position, sentenceList);
        }
    }

    private String formatName(String name) {
        if (!name.isEmpty()) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        if (name.endsWith(".")) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

    private List<Definition> mergeMapsOfNamesAndDefinitions(Map<Integer, List<String>> namesDefinitions,
                                                            Map<Integer, List<String>> infoDefinitions) {

        List<Definition> definitionList = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : namesDefinitions.entrySet()) {
            int page = entry.getKey();
            try {
                List<String> namesFromList = entry.getValue();
                List<String> infoListFromPage = infoDefinitions.get(page);
                for (int i = 0; i < namesFromList.size(); i++) {
                    Definition definition = new Definition(formatName(namesFromList.get(i)), infoListFromPage.get(i), 1);
                    definitionList.add(definition);
                    System.out.println(definition.getName() + " : " + definition.getInfo());
                    System.out.println(SEPARATOR);
                }
            } catch (Exception e) {
                System.out.println("Невозможно преобразовать");
            }
        }
        return definitionList;
    }

    private Map<Integer, List<String>> readInfoFromFile(String filePath) throws IOException {
        Map<Integer, List<String>> map = new HashMap<>(300);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int currentPage = 0;
            StringBuilder sentenceBuilder = new StringBuilder();
            String info = " ";
            while ((line = reader.readLine()) != null) {
                // надо ли выполнить сброс билдера
                if (line.startsWith("==== Page") || line.startsWith(SEPARATOR)) {
                    // если новая страница, то надо обновить счетчик
                    if (line.startsWith("==== Page")) {
                        currentPage = Integer.parseInt(line.split(" ")[2]);
                    }
                    info = sentenceBuilder.toString().toLowerCase().trim();

                    if (!info.isEmpty()) {
                        info = info.replaceAll("\\s*-\\s*", "");

                        if (line.startsWith(SEPARATOR)) {
                            // определение уже на новой странице, но мы не дошли до конца
                            addToHashMap(map, currentPage + 1, info);
                        } else {
                            addToHashMap(map, currentPage, info);
                        }
                        sentenceBuilder.setLength(0);
                    }
                } else {
                    sentenceBuilder.append(line);
                }
            }

            if (!info.isEmpty()) {
                info = info.replace("- ", "");
                addToHashMap(map, currentPage, info);
                sentenceBuilder.setLength(0);
            }
        }
        return map;
    }

    public List<Definition> extractDefinitions() throws IOException {
        String fileName = new File(filePath).getName();
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        String namesPath = OUTPUT_DIRECTORY + nameWithoutExtension + "_names.txt";
        String infoPath = OUTPUT_DIRECTORY + nameWithoutExtension + "_info.txt";

        System.out.println("Processing: " + filePath + " ...");
        PdfReader reader = null;
        try (PrintWriter outNameDefinitions = new PrintWriter(new FileOutputStream(namesPath));
             PrintWriter outInfoDefinitions = new PrintWriter(new FileOutputStream(infoPath))) {

            reader = new PdfReader(filePath);
            int numberOfPages = reader.getNumberOfPages();
            for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {

                String pageOfInfoDefinitions = PdfTextExtractor.getTextFromPage(reader, pageNumber, new ExtractInfoOfDefinitionStrategy());
                String pageNamesOfDefinitions = PdfTextExtractor.getTextFromPage(reader, pageNumber, new ExtractNamesOfDefinitionStrategy());

                if (pageOfInfoDefinitions.trim().length() > 0) {
                    String[] textLines = pageOfInfoDefinitions.split("[\r\n]");
                    for (int i = 0; i < textLines.length; i++) {
                        outInfoDefinitions.println(textLines[i]);
                        if (textLines[i].endsWith(".") && i != textLines.length - 1) {
                            outInfoDefinitions.println(SEPARATOR);
                        }
                    }

                    String[] definitions = pageNamesOfDefinitions.split("[\r\n]");
                    for (int i = 0; i < definitions.length; i++) {
                        if (i != 0 && Character.isUpperCase(definitions[i].charAt(0))) {
                            outNameDefinitions.println(SEPARATOR);
                        }
                        outNameDefinitions.println(definitions[i]);
                    }
                }
                outNameDefinitions.println("==== Page " + pageNumber + " =========================================================");
                outInfoDefinitions.println("==== Page " + pageNumber + " =========================================================");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        Map<Integer, List<String>> namesDefinitionsMap = readInfoFromFile(namesPath);
        Map<Integer, List<String>> infoDefinitionsMap = readInfoFromFile(infoPath);
        List<Definition> definitionList = mergeMapsOfNamesAndDefinitions(namesDefinitionsMap, infoDefinitionsMap);
        return definitionList;
    }
}
