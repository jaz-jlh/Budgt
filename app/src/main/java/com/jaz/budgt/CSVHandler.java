package com.jaz.budgt;

/**
 * Created by jaz on 9/20/17.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
    private InputStream inputStream;

    public CSVHandler(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public ArrayList read(){
        //todo make this general and customizeable
        ArrayList<String[]> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
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

}