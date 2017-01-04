package com.example.mohammed.toptenapps;

import android.nfc.Tag;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button btnParse;
    private ListView listApps;
    private String mFileContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParse=(Button) findViewById(R.id.btnParse);
        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseApplication parseApplications=new ParseApplication(mFileContent);
                parseApplications.process();
                ArrayAdapter <Application> arrayAdapter=new ArrayAdapter<Application>(
                        MainActivity.this,R.layout.list_item,parseApplications.getApplications());
                listApps.setAdapter(arrayAdapter);
            }
        });
        listApps=(ListView) findViewById(R.id.xmlListView);
        DownloadData downloadData=new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");






    }


    private class DownloadData extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            mFileContent=downloadXMLFile(strings[0]);
            if(mFileContent==null)
            {
                Log.d("DownloadData","Error downloading");
            }

            return mFileContent;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DownloadData","result: "+result);


        }

        private String downloadXMLFile(String urlPath){
            StringBuilder tempBuffer =new StringBuilder();
            try{

                URL url=new URL(urlPath);
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();
                int response=connection.getResponseCode();
                Log.d("DownloadData","the response code was "+response);
                InputStream is=connection.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);

                int charRead;
                char[] inputBuffer=new char[500];
                while (true)
                {
                    charRead=isr.read(inputBuffer);
                    if(charRead<=0) {
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer,0,charRead));
                }
                return tempBuffer.toString();

            }
            catch (IOException e){
                Log.d("DownloadData","IO Exception reading data: "+e.getMessage());
                e.printStackTrace();
            }catch (SecurityException e)
            {
                Log.d("DownloadData","Security Exception need permissions? "+e.getMessage());
            }
            return null;
        }

    }
}
