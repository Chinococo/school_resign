package com.example.http;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class bug extends AppCompatActivity {
    OkHttpClient.Builder _cilent;
    OkHttpClient cilent;
    HashMap<java.lang.String, List<Cookie>> cookieStore = new HashMap<>();
    String nowcookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);
        _cilent.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(final HttpUrl url, List<okhttp3.Cookie> cookies) {
                String cookie=cookies.toString();
                Log.e("url1", url.toString());
                Log.e("cookie", cookies.toString());
                cookieStore.put(url.host(), cookies);

            }

            @Override
            public List<okhttp3.Cookie> loadForRequest(HttpUrl url) {
                nowcookie=cookieStore.get(url.host()).toString();
                System.out.println(nowcookie);
                Log.e("url2", url.host());
                List<okhttp3.Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }
        );
        cilent=_cilent.build();
        cilent.cookieJar().loadForRequest(new HttpUrl.Builder().host("http://163.27.5.166/csn/Reg_Stu.ASP?CHOICE=OK&txtS_NO="+"713062"+ "&txtPass="+"Q124494446").build());

    }

}

