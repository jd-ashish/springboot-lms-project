package com.user.app;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eadge.extractpdfexcel.PdfConverter;
import org.eadge.extractpdfexcel.data.ExtractedData;
import org.eadge.extractpdfexcel.data.SortedData;
import org.eadge.extractpdfexcel.data.XclPage;
import org.eadge.extractpdfexcel.models.TextBlockIdentifier;
import org.eadge.extractpdfexcel.process.extraction.PdfParser;

import com.itextpdf.text.pdf.PdfReader;

public abstract class Pdf2Excel {
    public static ByteArrayOutputStream convert(InputStream inputStream) throws IOException {
        return convert(inputStream, null);
    }

    public static ByteArrayOutputStream convert(InputStream inputStream, String password) throws IOException {
        PdfReader pdf = password != null && password.length() > 0 ? new PdfReader(inputStream, password.getBytes()) : new PdfReader(inputStream);
        TextBlockIdentifier textBlockIdentifier = new TextBlockIdentifier();
        PdfParser parser = new PdfParser(pdf, textBlockIdentifier);
        parser.readAllPage();

        if (textBlockIdentifier.cleanDuplicated)
            parser.cleanDuplicatedData();
        if (textBlockIdentifier.mergeFactor > 1.0)
            parser.mergeBlocks(textBlockIdentifier.mergeFactor);

        parser.close();
        ExtractedData extractedData = parser.getExtractedData();
        SortedData sortedData = PdfConverter.sortExtractedData(extractedData, 0, 1);
        ArrayList<XclPage> excelPages = PdfConverter.createExcelPages(sortedData);
        HSSFWorkbook workbook = new HSSFWorkbook();
        ArrayList<HSSFSheet> sheets = new ArrayList<>();
        int page = 1;
        for (XclPage excelPage : excelPages) {
            HSSFSheet excelSheet = PdfConverter.createExcelSheet("page " + page, workbook, excelPage, 0, 0);
            sheets.add(excelSheet);
            page++;
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        workbook.write(outStream);
        return outStream;
    }
}
