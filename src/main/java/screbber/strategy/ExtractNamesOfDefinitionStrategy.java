package screbber.strategy;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class ExtractNamesOfDefinitionStrategy extends SimpleTextExtractionStrategy {

    boolean isDefinition(TextRenderInfo renderInfo) {
        if (!renderInfo.getFont().getPostscriptFontName().contains("Bold")
                && !renderInfo.getFont().getPostscriptFontName().contains("Italic")) {
            return false;
        }
        if (renderInfo.getFillColor() == null) {
            return false;
        }
        if (renderInfo.getFillColor().getRGB() != BaseColor.BLACK.getRGB()) {
            return false;
        }

        boolean isItalic = (renderInfo.getFont().getPostscriptFontName().contains("AJEUSD+DSFreeSet-BoldItalic"));

//        if (renderInfo.getFont().getPostscriptFontName().contains("AJEUSD+DSFreeSet-BoldItalic")) {
//            System.out.println(renderInfo.getText());
//        }
        return isItalic;
    }

    @Override
    public void renderText(TextRenderInfo renderInfo) {
        //System.out.println(renderInfo.getText() + ": ");
        for (String[] strings : renderInfo.getFont().getFamilyFontName()) {
            for (String font : strings) {
                // System.out.print(font + ", ");
            }
        }

        // System.out.print(renderInfo.getFont().toString());
        // System.out.print(renderInfo.getFillColor() == null ? "null" : renderInfo.getFillColor().getRGB() + " ");
        // System.out.println(BaseColor.BLACK.getRGB() + " " + BaseColor.BLUE.getRGB());

//        System.out.println();
//        System.out.println("Стиль : " + renderInfo.getFont().getPostscriptFontName());
//        System.out.println("-----------------------------");
        boolean isItalic = isDefinition(renderInfo);
        if (isItalic) {
            super.renderText(renderInfo);
        }
    }

}
