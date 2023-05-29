package screbber.strategy;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class ExtractInfoOfDefinitionStrategy extends SimpleTextExtractionStrategy {

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
//        boolean isItalic = (renderInfo.getFont().toString().contains("CMapAwareDocumentFont@2471cca7")
//        || renderInfo.getFont().toString().contains("CMapAwareDocumentFont@47d384ee"));

        boolean isItalic = (renderInfo.getFont().getPostscriptFontName().contains("GHJOYR+DSFreeSet-Bold")
                || renderInfo.getFont().getPostscriptFontName().contains("AJEUSD+DSFreeSet-BoldItalic"));

        if (renderInfo.getFont().getPostscriptFontName().contains("AJEUSD+DSFreeSet-BoldItalic")) {
           // System.out.println(renderInfo.getText());
        }
        return isItalic;
    }

    @Override
    public void renderText(TextRenderInfo renderInfo) {
        System.out.println(renderInfo.getText() + ": ");
        System.out.println("Стиль : " + renderInfo.getFont().getPostscriptFontName());
        System.out.println("-------------------------------------------------------");
        boolean isItalic = isDefinition(renderInfo);
        if (isItalic) {
            super.renderText(renderInfo);
        }
    }
}
