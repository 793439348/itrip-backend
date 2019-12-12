package cn.itrip.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @description: 提供了对url所指向的内容的加载操作
 * @author: zeng
 * @createDate: 2019-12-10
 * @version: v1.0
 */
public class UrlUtils {

    public static String loadURL(String ursStr) {
        try {
            URL url = new URL(ursStr);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            String responseStr = convertToString(inputStream);
            return responseStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertToString(InputStream inputStream)throws Exception{
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder result = new StringBuilder();
        String line = null;

        try {
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStreamReader!=null)
                inputStreamReader.close();
            if (inputStream !=null)
                inputStream.close();
            if(bufferedReader!=null)
                bufferedReader.close();
        }
        return result.toString();

    }
}
