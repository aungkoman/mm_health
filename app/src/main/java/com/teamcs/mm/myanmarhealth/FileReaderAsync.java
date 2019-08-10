package com.teamcs.mm.myanmarhealth;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.teamcs.mm.myanmarhealth.MainActivity.FILE_NAME;

public class FileReaderAsync  {

}
        /*

        extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        // Here you can show progress bar or something on the similar lines.
        // Since you are in a UI thread here.
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // After completing execution of given task, control will return here.
        // Hence if you want to populate UI elements with fetched data, do it here.
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        // You can track you progress update here
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Here you are in the worker thread and you are not allowed to access UI thread from here.
        // Here you can perform network operations or any heavy operations you want.

        File file = new File(getApplicationContext().getFilesDir(),FILE_NAME);
        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        String response = null;
        if(!file.exists()){
            try{
                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(json_str);
                bufferedWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        return null;
    }
}
*/