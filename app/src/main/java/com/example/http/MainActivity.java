package com.example.http;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText account, password;
    TextView preview;
    Button press;
    Spinner select;
    String STSTU, STNO01, STSTU0, time;
    String option = " _A20:校刊社          :0:40_A21:讀書社(一)      :2:50_A22:讀書社(二)      :0:50_A24:日語社          :0:40_A26:機器人研習社(二):0:30_A27:集郵社          :0:35_A28:電腦文書處理社  :0:35_A29:漫研社          :0:40_A30:棋弈社          :0:40_A31:橋弈社          :1:40_A32:攝影社          :0:40_A33:魔術社          :0:40_A34:社區服務桌遊社  :0:40_A35:春暉社          :0:50_A36:熱門音樂社      :0:50_A37:國樂社          :0:50_A38:康輔社          :0:50_A39:童軍社          :0:50_A40:吉他社          :0:90_A41:管樂社          :0:60_A42:烏克麗麗社      :0:60_A43:獨輪車社        :0:50_A44:籃球社(西)      :0:60_A45:籃球社(東)      :0:60_A46:排球社          :0:60_A48:飛盤社          :0:40_A49:游泳與健身社    :0:50_A50:初階羽球社      :0:45_A51:進階羽球社      :0:45_A52:桌球社          :0:45_A53:撞球社          :0:45_A54:壘球社          :0:45_A55:女子排球社      :0:45_A56:空手道社        :0:45_A58:運動傷害防護社  :0:30_A59:街舞社          :0:45_A60:滑板社          :0:45_A61:熱舞社          :0:45_A62:第二外語社      :0:35_A63:交服社          :0:50_A64:志工社          :0:50_A65:親善大使社      :0:40_A67:崇德青年社      :0:40_A69:足球社          :0:30_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setClass(this,bug.class);
        startActivity(intent);
        select = findViewById(R.id.spinner1);
        Date today = new Date();
        time = today.toString();
        System.out.println(time);
        select.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, option.split("_")));
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 0);
        //System.out.println();
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        preview = findViewById(R.id.preview);
        preview.setMovementMethod(ScrollingMovementMethod.getInstance());
        press = findViewById(R.id.press);
        press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!select.getSelectedItem().toString().equals(" "))
                {
                    STSTU0 = select.getSelectedItem().toString();
                    STNO01 = select.getSelectedItem().toString().substring(0, 3);
                }else
                {
                    STSTU0 = "";
                    STNO01 = "";
                }

                System.out.println(STSTU0);
                System.out.println(STNO01);
                new http(account.getText().toString(), password.getText().toString()).execute();
            }
        });

    }

    class http extends AsyncTask<String, Integer, String> {
        String cookie;
        boolean successful = true;
        String account;
        String password;
        String key;

        Response response;
        String get;
        HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<okhttp3.Cookie>>();

        http(String account, String password) {
            this.account = account;
            this.password = password;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (successful)
                    Toast.makeText(MainActivity.this, "已成功選社", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "此學生資料不存在或存在意外", Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<Pair<String, String>> d = new ArrayList<>();
            OkHttpClient okHttpClient;
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(final HttpUrl url, List<okhttp3.Cookie> cookies) {
                    cookie=cookies.toString();
                    Log.e("url1", url.toString());
                    Log.e("cookie", cookies.toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            key = url.host();
                        }
                    }).start();
                    cookieStore.put(url.host(), cookies);
                    if(!cookies.toString().contains("xLevel"))
                    successful=false;
                }

                @Override
                public List<okhttp3.Cookie> loadForRequest(HttpUrl url) {
                    Log.e("url2", url.host());
                    List<okhttp3.Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            });
            okHttpClient = builder.build();


            try {
                response = okHttpClient.newCall(new Request.Builder()
                        .url("http://163.27.5.166/csn/Reg_Stu.ASP?CHOICE=OK&txtS_NO=" + account + "&txtPass=" + password)
                        .header("User-Agent", "Mozilla/5.0")
                        .addHeader("Accept", "*/*")
                        .addHeader("Accept-Encoding", "gzip")
                        .build()).execute();
                // String test=new String(response.body().bytes(), "big5");
                //Log.e("123",test);
                if (account.equals("")||password.equals(""))
                    successful = false;
                else {
                    Log.e("request_start", "start");
                    response = okHttpClient.newCall(new Request.Builder()
                            .url("http://163.27.5.166/csn/ST/STSTU2.ASP")
                            .header("User-Agent", "Mozilla/5.0")
                            .addHeader("Accept", "*/*")
                            .addHeader("Accept-Encoding", "gzip")
                            .addHeader("Cookie", cookieStore.get(key).toString())
                            .build()).execute();
                    STSTU = Jsoup.parse(new String(response.body().bytes(), "big5")).select("#STSTU0").val();


                    okHttpClient.newCall(new Request.Builder()
                            .url("http://163.27.5.166/csn/ST/STSTU2.ASP?time=" + time + "&STSTU=" + STSTU + "&STNO01=&STNA01=")
                            .header("User-Agent", "Mozilla/5.0")
                            .addHeader("Accept", "*/*")
                            .addHeader("Accept-Encoding", "gzip")
                            .addHeader("Cookie", cookieStore.get(key).toString())
                            .build()).execute();


                    response = okHttpClient.newCall(new Request.Builder()
                            .url("http://163.27.5.166/csn/ST/STSTU2.ASP?Time=" + time + "&STSTU0=" + STSTU0 + "&STNO01=" + STNO01)
                            .header("User-Agent", "Mozilla/5.0")
                            .addHeader("Accept", "*/*")
                            .addHeader("Accept-Encoding", "gzip")
                            .addHeader("Cookie", cookieStore.get(key).toString())
                            .build()).execute();
                    String get = new String(response.body().bytes(), "big5");
                }
            } catch (Exception e) {
                //successful = false;
                e.printStackTrace();
            }


            return "null";

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}


