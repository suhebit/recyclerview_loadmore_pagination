package com.suheb.loadmore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.suheb.loadmore.listener.OnLoadMoreListener;
import com.suheb.loadmore.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnLoadMoreListener {
    private RecyclerView mRecyclerView;
    private List<User> mUsers = new ArrayList<>();
    private UserAdapter mUserAdapter;
    public boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        loadInitialData();
        setAdapter();
        setScrollListener();
    }

    private void setScrollListener() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    onLoadMore();
                    isLoading = true;
                }
            }
        });

    }

    private void setAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new UserAdapter(mUsers, MainActivity.this);
        mRecyclerView.setAdapter(mUserAdapter);
    }

    private void loadInitialData() {
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("Name " + i);
            user.setEmail("xyz" + i + "@gmail.com");
            mUsers.add(user);
        }
    }


    @Override
    public void onLoadMore() {
        Log.e("haint", "Load More");
        mUsers.add(null);
        mUserAdapter.notifyItemInserted(mUsers.size() - 1);

        //Load more data for reyclerview
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("haint", "Load More 2");

                //Remove loading item
                mUsers.remove(mUsers.size() - 1);
                mUserAdapter.notifyItemRemoved(mUsers.size());

                //Load data
                int index = mUsers.size();
                int end = index + 5;
                for (int i = index; i < end; i++) {
                    User user = new User();
                    user.setName("new items " + i);
                    user.setEmail("email" + i + "@gmail.com");
                    mUsers.add(user);
                }
                mUserAdapter.notifyDataSetChanged();
                setLoaded();
            }
        }, 2000);
    }

    public void setLoaded() {
        isLoading = false;
    }
}
