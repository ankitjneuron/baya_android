package com.baya.ApiRequest;

import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultipartRequest extends Request<String>
{
    private static final String STRING_PART_NAME = "text";
    final String BOUNDARY = "myboundary";
    private final Response.Listener<String> mListener;
    //     Bitmap bitmap;
    private final ArrayList<Bitmap> mFiles = new ArrayList<Bitmap>();
    Bitmap image;
    private MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
    private HashMap<String, String> mParams = new HashMap<String, String>();
    private String access_token;
    String hasimage="false";

    public MultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, Bitmap files, HashMap<String, String> params, String access_token,String hasimage)
    {
        super(Method.POST, url, errorListener);

        mListener = listener;
        this.image = files;
        this.access_token = access_token;
        this.hasimage=hasimage;
        mParams = params;

        entityBuilder.setBoundary(BOUNDARY);
        buildMultipartEntity();
    }

    private void buildMultipartEntity()
    {
        System.out.println("hasimage  "+hasimage);
if(hasimage.equals("true")) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.JPEG, 50, bos);
    entityBuilder.addPart("image", new ByteArrayBody(bos.toByteArray(), "fileField.jpg"));
}

        StringBuilder paramsBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> paramIterator = mParams.entrySet().iterator();
        while (paramIterator.hasNext())
        {

            Map.Entry<String, String> entry = paramIterator.next();
            System.out.println("data  "+entry.getValue());
            entityBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.TEXT_PLAIN));

        }
    }

    @Override
    public String getBodyContentType()
    {

        return "multipart/form-data;boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {



        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try
        {
            entityBuilder.build().writeTo(bos);
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {

        String jsonString;
        try
        {
            jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(jsonString, getCacheEntry());
        }
        catch (UnsupportedEncodingException e)
        {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access-token", access_token);
        return params;
    }

    @Override
    protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }
}