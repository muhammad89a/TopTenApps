package com.example.mohammed.toptenapps;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by mohammed on 04/01/2017.
 */

public class ParseApplication {
    private String xmlData;
    private ArrayList<Application> applications;


    public ParseApplication(String xmlData) {
        this.xmlData = xmlData;
        applications =new ArrayList<Application>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public boolean process(){
        boolean status=true;
        Application currentRecord=null  ;
        boolean inEntry=false;
        String textValue="";
        try {
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp=factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int evenType=xpp.getEventType();

            while (evenType!=XmlPullParser.END_DOCUMENT) {
                String tagName=xpp.getName();
                switch (evenType){
                    case XmlPullParser.START_TAG: {
                  //      Log.d("ParseApplications", "Starting tag for " + tagName);
                        if (tagName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            currentRecord = new Application();

                        }
                        break;
                    }
                    case XmlPullParser.TEXT:{
                        textValue=xpp.getText();
                        break;

                    }

                    case XmlPullParser.END_TAG: {
             //           Log.d("ParseApplications", "Ending tag for " + tagName);
                        if(inEntry){
                            if(tagName.equalsIgnoreCase("entry"))
                            {
                                applications.add(currentRecord);
                                inEntry=false;
                            }else if(tagName.equalsIgnoreCase("name")){
                                currentRecord.setName(textValue);
                            }else if(tagName.equalsIgnoreCase("artist")){
                                currentRecord.setArtist(textValue);
                            }else if(tagName.equalsIgnoreCase("releaseDate")){
                                currentRecord.setReleaseDate(textValue);
                            }
                        }

                        break;
                    }
                    default:
                        // nothing

                }
                evenType=xpp.next();
            }

        }
        catch (Exception e) {
            status=false;
            e.printStackTrace();
        }
        for(Application app:applications)
        {
            Log.d("ParseApplications","*******************");
            Log.d("ParseApplications","name: "+app.getName());
            Log.d("ParseApplications","artist: "+app.getArtist());
            Log.d("ParseApplications","releaseDate: "+app.getReleaseDate());
        }
        return true;
    }



}
