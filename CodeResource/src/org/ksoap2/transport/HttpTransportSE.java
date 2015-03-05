
package org.ksoap2.transport;

import java.io.*;
import java.net.*;
import java.util.List;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.xmlpull.v1.XmlPullParserException;

// Referenced classes of package org.ksoap2.transport:
//			Transport, ServiceConnectionSE, ServiceConnection

public class HttpTransportSE extends Transport {

    private ServiceConnection connection;

    public HttpTransportSE(String url) {
        super(null, url);
    }

    public HttpTransportSE(Proxy proxy, String url) {
        super(proxy, url);
    }

    public HttpTransportSE(String url, int timeout) {
        super(url, timeout);
    }

    public HttpTransportSE(Proxy proxy, String url, int timeout) {
        super(proxy, url, timeout);
    }

    public void call(String soapAction, SoapEnvelope envelope) throws IOException,
            XmlPullParserException {
        call(soapAction, envelope, null);
    }

    public List call(String soapAction, SoapEnvelope envelope, List headers) throws IOException,
            XmlPullParserException {
        if (soapAction == null)
            soapAction = "\"\"";
        byte requestData[] = createRequestData(envelope);
        requestDump = debug ? new String(requestData) : null;
        responseDump = null;
        connection = getServiceConnection();
        connection.setRequestProperty("User-Agent", "ksoap2-android/2.6.0+");
        if (envelope.version != 120)
            connection.setRequestProperty("SOAPAction", soapAction);
        if (envelope.version == 120)
            connection.setRequestProperty("Content-Type", "application/soap+xml;charset=utf-8");
        else
            connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
        connection.setRequestProperty("Connection", "close");
        connection.setRequestProperty("Content-Length", "" + requestData.length);
        if (headers != null) {
            for (int i = 0; i < headers.size(); i++) {
                HeaderProperty hp = (HeaderProperty) headers.get(i);
                connection.setRequestProperty(hp.getKey(), hp.getValue());
            }

        }
        connection.setRequestMethod("POST");
        connection.connect();
        OutputStream os = connection.openOutputStream();
        os.write(requestData, 0, requestData.length);
        os.flush();
        os.close();
        requestData = null;
        List retHeaders = null;
        InputStream is;
        try {
            connection.connect();
            is = connection.openInputStream();
            retHeaders = connection.getResponseProperties();
        } catch (IOException e) {
            is = connection.getErrorStream();
            if (is == null) {
                connection.disconnect();
                throw e;
            }
        }
        if (debug) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte buf[] = new byte[256];
            do {
                int rd = is.read(buf, 0, 256);
                if (rd == -1)
                    break;
                bos.write(buf, 0, rd);
            } while (true);
            bos.flush();
            buf = bos.toByteArray();
            responseDump = new String(buf);
            is.close();
            is = new ByteArrayInputStream(buf);
        }
        parseResponse(envelope, is);
        return retHeaders;
    }

    public ServiceConnection getConnection() {
        return (ServiceConnectionSE) connection;
    }

    protected ServiceConnection getServiceConnection() throws IOException {
        return new ServiceConnectionSE(proxy, url, timeout);
    }

    public String getHost() {
        String retVal = null;
        try {
            retVal = (new URL(url)).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public int getPort() {
        int retVal = -1;
        try {
            retVal = (new URL(url)).getPort();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public String getPath() {
        String retVal = null;
        try {
            retVal = (new URL(url)).getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
