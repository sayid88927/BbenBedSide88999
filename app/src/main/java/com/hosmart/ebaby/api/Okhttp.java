
package com.hosmart.ebaby.api;


import android.annotation.SuppressLint;

import com.blankj.utilcode.utils.LogUtils;
import com.hosmart.ebaby.api.persistentcookiejar.PersistentCookieJar;
import com.hosmart.ebaby.api.persistentcookiejar.cache.SetCookieCache;
import com.hosmart.ebaby.api.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.hosmart.ebaby.base.BaseApplication;
import com.blankj.utilcode.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


class Okhttp {

    private static PersistentCookieJar cookieJar;

    static OkHttpClient provideOkHttpClient() {

        //cookie
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getContext()));

        File httpCacheDir = new File(BaseApplication.getContext().getCacheDir(), "response");
        int cacheSize = 10 * 1024 * 1024;  //10 MiB
        Cache cache = new Cache(httpCacheDir, cacheSize);

        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .retryOnConnectionFailure(true) // 失败重发
//                .cache(cache)
//                .cookieJar(cookieJar)
//                .addInterceptor(new TokenInterceptor())
                //处理多BaseUrl,添加应用拦截器
//                .addInterceptor(new MoreBaseUrlInterceptor())
                .addInterceptor(new LoggingInterceptor());

        return builder.build();

    }

    private static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(365, TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();
            Request request = chain.request();
            if (!NetworkUtils.isAvailableByPing()) {
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isAvailableByPing()) {
                int maxAge = 0;//read from cache
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28;//tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .removeHeader("Prama")
                        .header("Cache-Control", "poublic, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    static class LoggingInterceptor implements Interceptor {
        @SuppressLint("DefaultLocale")
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();//请求发起的时间
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < request.url().queryParameterNames().size(); i++) {
                map.put(request.url().queryParameterName(i), request.url().queryParameterValue(i));
            }

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间

            ResponseBody responseBody = response.peekBody(1024 * 1024);

           LogUtils.e(String.format("接收响应: [%s] %n返回json:【%s】%n请求参数: [%s] %n响应时间[%.1fms]",
                    response.request().url(),
                    formatJson(responseBody.string()),
                    transMapToString(map),
                    (t2 - t1) / 1e6d
            ));

            return response;
        }
    }

    static String transMapToString(Map map) {
        Map.Entry entry;
        StringBuilder sb = new StringBuilder();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append(" == ").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "\n" : "");
        }
        return sb.toString();
    }


    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}