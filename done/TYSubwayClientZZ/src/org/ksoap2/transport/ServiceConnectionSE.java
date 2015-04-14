// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ServiceConnectionSE.java

package org.ksoap2.transport;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ksoap2.HeaderProperty;

// Referenced classes of package org.ksoap2.transport:
//			ServiceConnection

public class ServiceConnectionSE implements ServiceConnection {

    private HttpURLConnection connection;

    public ServiceConnectionSE(String url) throws IOException {
        this(null, url, 20000);
    }

    public ServiceConnectionSE(Proxy proxy, String url) throws IOException {
        this(proxy, url, 20000);
    }

    public ServiceConnectionSE(String url, int timeout) throws IOException {
        this(null, url, timeout);
    }

    public ServiceConnectionSE(Proxy proxy, String url, int timeout) throws IOException {
        connection = proxy != null ? (HttpURLConnection) (new URL(url)).openConnection(proxy)
                : (HttpURLConnection) (new URL(url)).openConnection();
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
    }

    public void connect() throws IOException {
        connection.connect();
    }

    public void disconnect() {
        connection.disconnect();
    }

    public List getResponseProperties() {
        Map properties = connection.getHeaderFields();
        Set keys = properties.keySet();
        List retList = new LinkedList();
        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = (String) i.next();
            List values = (List) properties.get(key);
            int j = 0;
            while (j < values.size()) {
                retList.add(new HeaderProperty(key, (String) values.get(j)));
                j++;
            }
        }

        return retList;
    }

    public void setRequestProperty(String string, String soapAction) {
        connection.setRequestProperty(string, soapAction);
    }

    public void setRequestMethod(String requestMethod) throws IOException {
        connection.setRequestMethod(requestMethod);
    }

    public OutputStream openOutputStream() throws IOException {
        return connection.getOutputStream();
    }

    public InputStream openInputStream() throws IOException {
        return connection.getInputStream();
    }

    public InputStream getErrorStream() {
        return connection.getErrorStream();
    }

    public String getHost() {
        return connection.getURL().getHost();
    }

    public int getPort() {
        return connection.getURL().getPort();
    }

    public String getPath() {
        return connection.getURL().getPath();
    }
}
