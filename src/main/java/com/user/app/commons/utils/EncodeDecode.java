package com.user.app.commons.utils;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.user.app.commons.constants.AppConstants.EncodeDecodeTimes;

public class EncodeDecode {

    public static String encode(String data) {
        String loop_data = data;
        for(int i = 0; i < EncodeDecodeTimes; i++){
            loop_data = commonEncode(loop_data);
        }
        return loop_data;
    }

    protected static String commonEncode(String data){
        byte[] bytesEncoded = Base64.getEncoder().encode(data.getBytes());
        return new String(bytesEncoded, StandardCharsets.ISO_8859_1);
    }

    protected static String commonDecode(String data){
        byte[] valueDecoded = Base64.getDecoder().decode(data);
        return new String(valueDecoded,StandardCharsets.ISO_8859_1);
    }
    public static String decode(String data) {
        return timesDecode(data, EncodeDecodeTimes);
    }
    public static String decode(String data,int times) {
        return timesDecode(data,times);
    }
    private static String timesDecode(String data,int times) {
        String loop_data = data;
        for(int i = 0; i < times; i++){
            loop_data = commonDecode(loop_data);
        }
        return loop_data;
    }
    public static String decodeV2(String data) {
        String loop_data = data;
        for(int i = 0; i < EncodeDecodeTimes*2; i++){
            loop_data = commonDecode(loop_data);
        }
        return loop_data;
    }

}
