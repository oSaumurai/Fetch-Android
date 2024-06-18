package com.fetch.oa;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fetch.oa.model.ListItem;
import com.fetch.oa.view.ListItemAdapter;
import com.fetch.oa.view.ListItemViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListItemAdapter itemAdapter;
    private ListItemViewModel itemViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemViewModel = new ViewModelProvider(this).get(ListItemViewModel.class);

        itemViewModel.getItems().observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> items) {
                itemAdapter = new ListItemAdapter(items);
                recyclerView.setAdapter(itemAdapter);
            }
        });

        itemViewModel.fetchData("https://fetch-hiring.s3.amazonaws.com/hiring.json");
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}