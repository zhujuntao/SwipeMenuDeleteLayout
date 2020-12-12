package com.example.swipemenudeletelayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.swipemenudeletelayout.view.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRv;
    private FullDelDemoAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<SwipeBean> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = findViewById(R.id.rv);
        initDatas();
        mAdapter = new FullDelDemoAdapter(this, mDatas);
        mAdapter.setOnDelListener(new FullDelDemoAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < mDatas.size()) {
                    Toast.makeText(MainActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                    mDatas.remove(pos);
                    mAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTop(int pos) {
                if (pos > 0 && pos < mDatas.size()) {
                    SwipeBean swipeBean = mDatas.get(pos);
                    mDatas.remove(swipeBean);
                    mAdapter.notifyItemInserted(0);
                    mDatas.add(0, swipeBean);
                    mAdapter.notifyItemRemoved(pos + 1);
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mRv.scrollToPosition(0);
                    }
                    //notifyItemRangeChanged(0,holder.getAdapterPosition()+1);
                }
            }

            @Override
            public void onRead(int pos) {
                if (pos >= 0 && pos < mDatas.size()) {
                    Toast.makeText(MainActivity.this, "已读:" + pos, Toast.LENGTH_SHORT).show();
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }
        });
        mRv.setAdapter(mAdapter);
//        mRv.setLayoutManager(mLayoutManager = new GridLayoutManager(this, 2));
        mRv.setLayoutManager(mLayoutManager = new LinearLayoutManager(this));

        //6 2016 10 21 add , 增加viewChache 的 get()方法，
        // 可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。我个人觉得意义不大，
        mRv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });
    }
    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new SwipeBean("" + i));
        }
    }
}