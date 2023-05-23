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

        return isItalic;
    }

    @Override
    public void renderText(TextRenderInfo renderInfo) {
        boolean isItalic = isDefinition(renderInfo);
        if (isItalic) {
            super.renderText(renderInfo);
        }
    }

}
