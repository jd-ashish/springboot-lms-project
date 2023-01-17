package com.user.app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jayway.jsonpath.ParseContext;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.hibernate.boot.Metadata;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, DocumentException {
        String path = "/home/";
        StringBuilder sb = new StringBuilder();

        String json = "[What will be the output of the following Java program?" +
                "\\n\\nclass output {\\n        public static void main(String args[]) \\n      " +
                "  {\\n            double a, b,c;\\n          " +
                "  a \\u003d 3.0/0;\\n         " +
                "   b \\u003d 0/4.0;\\n          " +
                "  c\\u003d0/0.0;\\n \\n     " +
                "System.out.println(a);\\n     " +
                "       System.out.println(b);\\n    " +
                "        System.out.println(c);\\n        } \\n    }" +
                "" +
                ", NaN, Infinity, 0.0 , all of the mentioned, d]";

        System.out.println(json);

//
//        for (String cmd : path.split("/")) {
//            switch (cmd) {
//                case "":
//                case ".":
//                    break;
//                case "..":
//                    while (sb.length() > 0 && sb.charAt(sb.length() - 1) != '/') {
//                        sb.deleteCharAt(sb.length() - 1);
//                    }
//                    if (sb.length() > 0) {
//                        sb.deleteCharAt(sb.length() - 1);
//                    }
//                    break;
//                default:
//                    sb.append('/').append(cmd);
//            }
//        }
//
//        if (sb.length() == 0) {
//            System.out.println( "/");
//        }
//        try {
//            System.out.println(convertDateFromTzFormatV2("2022-12-20T06:27:57.856923Z",1));
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
    }
    public static String convertDateFromTzFormatV2(String dateStr, int formatType) throws ParseException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat destFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        SimpleDateFormat destTwoFormat = new SimpleDateFormat("dd-MM-yyyy, hh:mm a");
        SimpleDateFormat destThreeFormat = new SimpleDateFormat("dd MMM''yy, hh:mm a");
        sourceFormat.setTimeZone(utc);
        Date convertedDate = sourceFormat.parse(dateStr);
        String[] formattedDate = destFormat.format(convertedDate).replaceAll("am", "AM").replaceAll("pm", "PM").split(", ");
        String[] formattedDateTwo = destTwoFormat.format(convertedDate).replaceAll("am", "AM").replaceAll("pm", "PM").split(", ");
        String[] formattedDateThree = destThreeFormat.format(convertedDate).replaceAll("am", "AM").replaceAll("pm", "PM").split(", ");

        String d = null;

        if (formatType == 1){
            d = formattedDate[1]+" on "+formattedDate[0]; // 11:31 AM on 07 Dec 2022
        }else if(formatType == 2){
            d = formattedDateTwo[0]+" \u00b7 "+ formattedDateTwo[1]; // 07-12-2022 . 05:22 PM
        }else if(formatType == 3){
            d = formattedDate[0] + " \u00b7 " + formattedDate[1]; // 07 Dec 2022 . 05:22 PM
        }else if(formatType == 4) {
            d = formattedDateTwo[0]+"  "+ formattedDateTwo[1]; // 07-12-2022 11:31 PM
        }else if(formatType == 5) {
            d = formattedDate[0]; // 07 Dec 2022
        }else if(formatType == 6){
            d = formattedDateThree[0]+" "+formattedDateThree[1]; // 25 Nov'22 12:09 PM
        }
        return  d;
    }


}
