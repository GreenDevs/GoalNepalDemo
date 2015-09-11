package com.crackdevelopers.goalnepal;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by trees on 9/10/15.
 */
public class FileManager
{
    private Context context;
    private static final String FILE_NAME="menu.json";

    public FileManager(Context context)
    {
        this.context=context;
    }

    public  void writeToFile(String data)
    {
        try
        {
            OutputStreamWriter writer=new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
            writer.write(data);
            writer.close();
        }
        catch (IOException e)
        {
            Log.e("EXCEPTION", "File write failed");
            e.printStackTrace();
        }
    }


    public String readFromFile()
    {

        String content="";

        try
        {
            InputStream inputStream=context.openFileInput(FILE_NAME);

            if(inputStream!=null)
            {
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                String recievedString="";

                StringBuilder stringBuilder=new StringBuilder();

                while ((recievedString=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(recievedString);
                }

                inputStream.close();
                content=stringBuilder.toString();


            }
        } catch (IOException e  )
        {
            e.printStackTrace();
        }

        return  content;
    }
}
