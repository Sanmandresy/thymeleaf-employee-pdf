package mg.management.employee.service.util;

import com.lowagie.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class PdfUtils {
  private PdfUtils() {

  }

  public static byte[] generatePdf(String content) throws IOException, DocumentException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ITextRenderer renderer = new ITextRenderer();
    renderer.setDocumentFromString(content);
    renderer.layout();
    renderer.createPDF(outputStream);
    outputStream.close();
    return outputStream.toByteArray();
  }
}
