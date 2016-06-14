package com.rai.umesh.cmrlhelper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/*
* This prepares the GET string for the Volley request
* B. Umesh Rai
 */
public class AssetDecipherRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://cmrlvent.co.in/assetMaint/api/web/asset-code/decipher?assetCode=";

    public AssetDecipherRequest (String assetCode, Response.Listener<String> listener){
        super(Request.Method.GET, LOGIN_REQUEST_URL + assetCode, listener, null);
    }
}
