package com.blogspot.zongjia.pttmarvel.api;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.blogspot.zongjia.pttmarvel.databinding.ArticlePushBlockItemBinding;
import com.blogspot.zongjia.pttmarvel.model.post.PttPostPush;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IpListService {
    public static TreeMap<String, String> tempIpMap = new TreeMap<String, String>();

    public static CompositeDisposable compositeDisposable = new CompositeDisposable();
    @BindingAdapter("IpLocationText")
    public static void ipLocatoinText(TextView textView, ArticlePushBlockItemBinding binding) {
        PttPostPush push = binding.getPushBlock();
        String ipText = push.ip;
        if (ipText == "") return;

        Log.d("推文IP查詢觸發", String.format("disposable's size = %d", compositeDisposable.size()));
        if (compositeDisposable.size() > 20) {
            compositeDisposable.clear();
        }
        Disposable disposable = Observable.just(ipText).
                observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String ip) throws Exception {
                        return getIpLocationCode(ip);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.contains("TW")) {
                            push.isTaiwan = true;
                            binding.taiwanIcon.setVisibility(View.VISIBLE);
                            binding.pushIpLocationText.setTextColor(Color.WHITE);
                        } else {
                            push.isTaiwan = false;
                            binding.pushIpLocationText.setTextColor(Color.RED);
                            binding.taiwanIcon.setVisibility(View.INVISIBLE);
                        }
                        binding.pushIpLocationText.setText(s);

                    }
                });
        compositeDisposable.add(disposable);

    }

    @BindingAdapter("ipIconVisiable")
    public static void ipIconVisiable(ImageView imageView, PttPostPush push) {
        if (push.isTaiwan) {
            imageView.setVisibility(View.VISIBLE);
        } else {

            imageView.setVisibility(View.INVISIBLE);
        }

    }
    private static String getIpLocationCode(String ip) {
        if (tempIpMap.containsKey(ip) && !ip.contains("No")) {
            return tempIpMap.get(ip);
        }
        // 如果map中沒有此組IP的地區資訊的話，網路請求
        String locationCode = requestIpLocation(ip);
        if (tempIpMap.size() > 200) {
            tempIpMap.clear();
        }
        tempIpMap.put(ip, locationCode);

        return locationCode;
    }
    private static String requestIpLocation(String ip) {
        OkHttpClient client = new OkHttpClient();

        Log.d("ipLocation", "IP地址請求中");

        String requestUrl = String.format("https://iplist.cc/api/%s", ip);
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String responseString = response.body().string();
                JSONObject jsonObject = new JSONObject(responseString);
                return (String) jsonObject.get("countrycode");
            } else {
                Log.d("JSons", "response.body() == null");

            }
        } catch (Exception e) {
            Log.d("JsonException", String.format(e.toString() + "url='%s'", requestUrl));
            return "No Ip";
        }
        return "No Ip 2";
    }
}
