package com.jaz.budgt;

/**
 * Created by jaz on 9/20/17.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {

    public static ArrayList<String[]> readFromResource(InputStream inputStream, String splitToken){

        ArrayList<String[]> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(splitToken);
                resultList.add(row);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    public static ArrayList<String[]> readFromExternalFile(BufferedReader reader, String splitToken){

        ArrayList<String[]> resultList = new ArrayList<>();
        if(reader != null) {
            try {
                String csvLine;
                while ((csvLine = reader.readLine()) != null) {
                    String[] row = csvLine.split(splitToken);
                    resultList.add(row);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }

    public static boolean write(FileOutputStream fileOutputStream, String data) {
        try {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}