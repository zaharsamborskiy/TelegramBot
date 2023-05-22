package ru.samborskiy.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Service
@PropertySource("application.properties")
public class CurrencyService {
    @Value("${api.key}")
    private String apikey;
    public Response getCurrencyRate(String messageFrom, String messageTo, String amount) throws IOException {
        String url = "https://api.apilayer.com/fixer/convert?to=" + messageTo + "&from=" + messageFrom + "&amount=" + amount;
        OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(300, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                    .url(url)
                    .addHeader("apikey", apikey).get().build();

        return client.newCall(request).execute();
    }
}
