package com.gmail.maxdiland.earthquakeviewer;

import android.app.ListFragment;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.gmail.maxdiland.earthquakeviewer.entity.Quake;
import com.gmail.maxdiland.earthquakeviewer.util.HttpUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * author Max Diland
 */
public class EarthQuakeListFragment extends ListFragment {
    private ArrayAdapter<Quake> arrayAdapter;
    private ArrayList<Quake> earthQuakes = new ArrayList<Quake>();

    private Handler handler = new Handler();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        arrayAdapter = new ArrayAdapter<Quake>(
                getActivity(), android.R.layout.simple_list_item_1, earthQuakes);

        setListAdapter(arrayAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshEarthquakes();
            }
        }).start();

    }

    public void refreshEarthquakes() {
        String dataResource = getString(R.string.earthQuakeDataResource);

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(
                    new ByteArrayInputStream(HttpUtil.getBodyBytesFromResource(dataResource))
            );

            Element documentElement = document.getDocumentElement();

            earthQuakes.clear();

            NodeList nodeList = documentElement.getElementsByTagName("entry");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element entry = (Element) nodeList.item(i);
                    Element title = (Element) entry.getElementsByTagName("title").item(0);
                    Element pointElem = (Element) entry.getElementsByTagName("georss:point").item(0);
                    Element when = (Element) entry.getElementsByTagName("updated").item(0);
                    Element link = (Element) entry.getElementsByTagName("link").item(0);

                    String details = title.getFirstChild().getNodeValue();
                    String hostname = "http://earthquake.usgs.gov";
                    String linkString = hostname + link.getAttribute("href");

                    if (pointElem == null) {
                        continue;
                    }

                    String point = pointElem.getFirstChild().getNodeValue();
                    String date = when.getFirstChild().getNodeValue();

                    SimpleDateFormat simpleDateFormat =
                            new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                    Date quakeDate = new GregorianCalendar(0, 0, 0).getTime();
                    try {
                        quakeDate = simpleDateFormat.parse(date);
                    } catch (ParseException e) {
                        Log.d("No tag", "Cannot parse date", e);
                    }

                    String[] rawLocation = point.split(" ");
                    Location location = new Location("dummyGPS");
                    location.setLatitude(Double.parseDouble(rawLocation[0]));
                    location.setLongitude(Double.parseDouble(rawLocation[1]));

                    String rawMagnitude = details.split(" ")[1].split(",")[0];
                    double magnitude;
                    try {
                        magnitude = Double.parseDouble(rawMagnitude);
                    } catch (NumberFormatException nfe) {
                        Log.d("No tag", "Cannot parse number");
                        continue;
                    }

                    if (details.indexOf(',') > -1) {
                        details = details.split(",")[1].trim();
                    }

                    final Quake quake =
                            new Quake(quakeDate, details, location, magnitude, linkString);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            addNewQuake(quake);
                        }
                    });
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addNewQuake(Quake quake) {
        if ( ((EarthQuakeActivity) getActivity()).getMinimumMagnitude() < quake.getMagnitude() ) {
            earthQuakes.add(quake);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
