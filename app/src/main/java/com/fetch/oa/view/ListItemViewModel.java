package com.fetch.oa.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fetch.oa.MyApplication;
import com.fetch.oa.model.ListItem;
import com.google.gson.Gson;

import java.util.List;
import android.os.Handler;
import android.os.Looper;
import okhttp3.OkHttpClient;
import java.util.stream.Collectors;

import android.util.Log;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class ListItemViewModel extends ViewModel {
    private MutableLiveData<List<ListItem>> itemsLiveData = new MutableLiveData<>();
    private final OkHttpClient client = new OkHttpClient();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LiveData<List<ListItem>> getItems() {
        return itemsLiveData;
    }

    public void fetchData(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainHandler.post(() ->
                        Toast.makeText(MyApplication.getContext(), "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
                Log.e("Connection","failed to fetch data");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String json = response.body().string();
                    List<ListItem> items = new Gson().fromJson(json, new TypeToken<List<ListItem>>(){}.getType());

                    // Filter and sort items
                    items = items.stream()
                            .filter(item -> item.getName() != null && !item.getName().isEmpty())
                            .collect(Collectors.toList());

                    items.sort((item1, item2) -> {
                        int compareListId = Integer.compare(item1.getListId(), item2.getListId());
                        if (compareListId == 0) {
                            return item1.getName().compareTo(item2.getName());
                        } else {
                            return compareListId;
                        }
                    });

                    itemsLiveData.postValue(items);
                } else {
                    mainHandler.post(() ->
                            Toast.makeText(MyApplication.getContext(), "Failed to fetch data: Response not successful", Toast.LENGTH_SHORT).show()
                    );
                    Log.e("Connection","failed to fetch data");

                }
            }
        });
    }
}
