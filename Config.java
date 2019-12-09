package com.example.myapplication;

/**
 * Created by sideeg on 2/22/2018.
 */
public class Config {

    public static final String id = "https://taxiappsideeg.000webhostapp.com/enzar/";
//    public static final String id = "https://http://enzarapp.epizy.com/enzar/";
//       public static final String id = "http://192.168.43.110/enzar/";
    // adrees of our scripts
    public static final String URL_CHECK_USER  = id + "enzarsignin.php";
    public static final String URL_REQUEST  = id +"enzarrequest.php";
    public static final String URL_REQUEST_NAME  = id +"getname.php";

    //keys used to send data to the php server
    public static final String KEY_ID = "id";
    public static final String KEY_USER_PHONE  = "phone";
    public static final String KEY_GROUP_NUMBER = "groupNumber";



    //josn tags
    public static final String TAG_JASON_ARRAY  = "result";
    public static final String TAG_name  = "name";
    public static final String TAG_ID = "id";
    public static final String TAG_GROUpNUMBER  = "groupNumber";
    public static final String TAG_DRIVER_NAME  = "name";
    public static final String TAG_pHONE  = "phone";



 public static final String PREFERENCES_PASSENGER_ID = "id";
    public static final String PREFERENCES_PASSENGER_GROUPNUmBER = "groupNumber";
    public static final String PREFERENCES_Request_GROUPNUmBER = "requestgroupNumber";
    public static final String PREFERENCES_PASSENGER_NAME = "name";
    public static final String PREFERENCES_request_PASSENGER_NAME = "requestname";
    public static final String PREFERENCES_request_PASSENGER_PHONE = "requestphone";
    public static final String PREFERENCES_PASSENGER_PHONE = "phone";
}
