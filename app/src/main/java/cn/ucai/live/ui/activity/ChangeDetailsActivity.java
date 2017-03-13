package cn.ucai.live.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.live.R;
import cn.ucai.live.data.NetDao;
import cn.ucai.live.data.model.RechargeStatements;
import cn.ucai.live.data.model.Result;
import cn.ucai.live.ui.GridMarginDecoration;
import cn.ucai.live.utils.OnCompletListener;
import cn.ucai.live.utils.ResultUtils;

public class ChangeDetailsActivity extends AppCompatActivity {
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;

    ArrayList<RechargeStatements> mList;
    ChangeDetailsAdapter adapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_details);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(ChangeDetailsActivity.this);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.addItemDecoration(new GridMarginDecoration(10));
        mRv.setLayoutManager(mLayoutManager);
        mList = new ArrayList<>();
        adapter = new ChangeDetailsAdapter(mList, ChangeDetailsActivity.this);
        mRv.setAdapter(adapter);

        mSrl = (SwipeRefreshLayout) findViewById(R.id.srl);
        mTvRefresh = (TextView) findViewById(R.id.tv_refresh);
    }

    private void initData() {
        NetDao.getRechargeStatements(ChangeDetailsActivity.this, EMClient.getInstance().getCurrentUser(), 1, 10,
                new OnCompletListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        mSrl.setRefreshing(false);
                        mTvRefresh.setVisibility(View.GONE);

                        if (s != null) {
                            Result result = ResultUtils.getListResultFromJson(s, RechargeStatements.class);
                            if (result != null && result.isRetMsg()) {
                                ArrayList<RechargeStatements> list = (ArrayList<RechargeStatements>) result.getRetData();
                                if (list != null && list.size() > 0) {
                                    adapter.addData(list);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    static class ChangeDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        ArrayList<RechargeStatements> mList;

        public void initData(ArrayList<RechargeStatements> list) {
            if (mList != null) {
                mList.clear();
            }
            addData(list);
        }

        public void addData(ArrayList<RechargeStatements> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

        public ChangeDetailsAdapter(ArrayList<RechargeStatements> list, Context context) {
            this.mList = list;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_change_detail, null);
            return new ChangeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RechargeStatements statements = mList.get(position);
            ChangeViewHolder viewHolder = (ChangeViewHolder) holder;
            viewHolder.mTvName.setText("充值用户："+statements.getUname());
            viewHolder.mTvCount.setText("充值数量:"+statements.getRcount());
            viewHolder.mTvRmb.setText("充值金额:"+statements.getRmb());

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String t = format.format(new Date(statements.getRdate()));
            viewHolder.mTvTime.setText("充值时间:"+t);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        static class ChangeViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_name)
            TextView mTvName;
            @BindView(R.id.tv_count)
            TextView mTvCount;
            @BindView(R.id.tv_rmb)
            TextView mTvRmb;
            @BindView(R.id.tv_time)
            TextView mTvTime;

            ChangeViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
