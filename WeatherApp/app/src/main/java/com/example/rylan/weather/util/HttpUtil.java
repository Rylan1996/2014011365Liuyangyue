package com.example.rylan.weather.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rylan on 2016/10/20.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                try {
                    URL url = new URL(address);
                    LogUtil.log("sendHttpRequest", address, LogUtil.DEBUG);
                    LogUtil.log("sendHttpRequest", url.toString(), LogUtil.DEBUG);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//                    StringBuilder response = new StringBuilder();
//                    String line = null;
//
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }

                    if (listener != null) {
                        // 回调onFinish()方法
                        // onFinish()方法中传入 connection.getInputStream() 得到的服务器返回的输入流
                        listener.onFinish(in);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 回调onError()方法
                    listener.onError(e);
                } finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}
