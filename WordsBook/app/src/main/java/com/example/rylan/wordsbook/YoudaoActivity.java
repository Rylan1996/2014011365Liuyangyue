package com.example.rylan.wordsbook;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class YoudaoActivity extends Activity {
    private EditText edit = null;
    private TextView search = null;
    private TextView text = null;
    private String YouDaoBaseUrl = "http://fanyi.youdao.com/openapi.do";
    private String YouDaoKeyFrom = "WordsBook";
    private String YouDaoKey = "1383895790";
    private String YouDaoType = "data";
    private String YouDaoDoctype = "json";
    private String YouDaoVersion = "1.1";
    private TranslateHandler handler;

    private static final int SUCCEE_RESULT = 10;
    private static final int ERROR_TEXT_TOO_LONG = 20;
    private static final int ERROR_CANNOT_TRANSLATE = 30;
    private static final int ERROR_UNSUPPORT_LANGUAGE = 40;
    private static final int ERROR_WRONG_KEY = 50;
    private static final int ERROR_WRONG_RESULT = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_youdao);

        edit = (EditText) findViewById(R.id.searchcontent);
        search = (TextView) findViewById(R.id.btn_search);
        search.setOnClickListener(new searchListener());
        text = (TextView) findViewById(R.id.searchresult);
        handler = new TranslateHandler(this, text);
    }

    private class searchListener implements OnClickListener {
        @Override
        public void onClick(View v) {

            String content = edit.getText().toString().trim();
            if (content == null || "".equals(content)) {
                Toast.makeText(getApplicationContext(), "请输入要翻译的内容", Toast.LENGTH_SHORT).show();
                return;
            }
            final String YouDaoUrl = YouDaoBaseUrl + "?keyfrom=" + YouDaoKeyFrom + "&key=" + YouDaoKey + "&type="
                    + YouDaoType + "&doctype=" + YouDaoDoctype + "&type=" + YouDaoType + "&version=" + YouDaoVersion
                    + "&q=" + content;
            new Thread() {
                public void run() {
                    try {
                        AnalyzingOfJson(YouDaoUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
            }.start();
        }
    }

    private void AnalyzingOfJson(String url) throws Exception {
        // 创建HttpGet对象
        HttpGet httpGet = new HttpGet(url);
        // 使用execute方法发送HTTP GET请求，并返回HttpResponse对象
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            // 使用getEntity方法活得返回结果
            String result = EntityUtils.toString(httpResponse.getEntity());
            System.out.println("result:" + result);
            JSONArray jsonArray = new JSONArray("[" + result + "]");
            String message = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject != null) {
                    String errorCode = jsonObject.getString("errorCode");
                    if (errorCode.equals("20")) {
                        handler.sendEmptyMessage(ERROR_TEXT_TOO_LONG);
                    } else if (errorCode.equals("30 ")) {
                        handler.sendEmptyMessage(ERROR_CANNOT_TRANSLATE);
                    } else if (errorCode.equals("40")) {
                        handler.sendEmptyMessage(ERROR_UNSUPPORT_LANGUAGE);
                    } else if (errorCode.equals("50")) {
                        handler.sendEmptyMessage(ERROR_WRONG_KEY);
                    } else {
                        Message msg = new Message();
                        msg.what = SUCCEE_RESULT;
                        // 要翻译的内容
                        String query = jsonObject.getString("query");
                        message = "翻译结果：";
                        // 翻译内容
                        Gson gson = new Gson();
                        Type lt = new TypeToken<String[]>() {
                        }.getType();
                        String[] translations = gson.fromJson(jsonObject.getString("translation"), lt);
                        for (String translation : translations) {
                            message += "\t" + translation;
                        }
                        // 有道词典-基本词典
                        if (jsonObject.has("basic")) {
                            JSONObject basic = jsonObject.getJSONObject("basic");
                            if (basic.has("phonetic")) {
                                String phonetic = basic.getString("phonetic");
                                // message += "\n\t" + phonetic;
                            }
                            if (basic.has("explains")) {
                                String explains = basic.getString("explains");
                                // message += "\n\t" + explains;
                            }
                        }

                        if (jsonObject.has("web")) {
                            String web = jsonObject.getString("web");
                            JSONArray webString = new JSONArray("[" + web + "]");
                            message += "\n网络释义：";
                            JSONArray webArray = webString.getJSONArray(0);
                            int count = 0;
                            while (!webArray.isNull(count)) {

                                if (webArray.getJSONObject(count).has("key")) {
                                    String key = webArray.getJSONObject(count).getString("key");
                                    message += "\n（" + (count + 1) + "）" + key + "\n";
                                }
                                if (webArray.getJSONObject(count).has("value")) {
                                    String[] values = gson.fromJson(webArray.getJSONObject(count).getString("value"),
                                            lt);
                                    for (int j = 0; j < values.length; j++) {
                                        String value = values[j];
                                        message += value;
                                        if (j < values.length - 1) {
                                            message += "，";
                                        }
                                    }
                                }
                                count++;
                            }
                        }
                        msg.obj = message;
                        handler.sendMessage(msg);
                    }
                }
            }
            text.setText(message);
        } else {
            handler.sendEmptyMessage(ERROR_WRONG_RESULT);
        }
    }

    private class TranslateHandler extends Handler {
        private Context mContext;
        private TextView mTextView;

        public TranslateHandler(Context context, TextView textView) {
            this.mContext = context;
            this.mTextView = textView;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCEE_RESULT:
                    mTextView.setText((String) msg.obj);
                    closeInput();
                    break;
                case ERROR_TEXT_TOO_LONG:
                    Toast.makeText(mContext, "要翻译的文本过长", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CANNOT_TRANSLATE:
                    Toast.makeText(mContext, "无法进行有效的翻译", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_UNSUPPORT_LANGUAGE:
                    Toast.makeText(mContext, "不支持的语言类型", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_WRONG_KEY:
                    Toast.makeText(mContext, "无效的key", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_WRONG_RESULT:
                    Toast.makeText(mContext, "提取异常", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if ((inputMethodManager != null) && (this.getCurrentFocus() != null)) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
