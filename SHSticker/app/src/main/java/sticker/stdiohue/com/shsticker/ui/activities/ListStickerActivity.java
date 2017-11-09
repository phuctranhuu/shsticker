package sticker.stdiohue.com.shsticker.ui.activities;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import sticker.stdiohue.com.shsticker.R;
import sticker.stdiohue.com.shsticker.databinding.ActivityListStickerBinding;
import sticker.stdiohue.com.shsticker.eventhandlers.ListStickerActivityEH;
import sticker.stdiohue.com.shsticker.ui.adapters.StickerAdapter;
import sticker.stdiohue.com.shsticker.ui.bases.BaseActivity;

/**
 * Created by tran.huu.phuc(tranhuuphuc20051995@gmail.com) on 09/11/2017.
 */

public class ListStickerActivity extends BaseActivity<ActivityListStickerBinding> implements StickerAdapter.StickerListener, ListStickerActivityEH {
    private List<Integer> mData;
    private StickerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_sticker;
    }

    @Override
    protected void init() {
        addSticker();
        mAdapter = new StickerAdapter(mData, this);
        viewDataBinding.rvSticker.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        viewDataBinding.rvSticker.setAdapter(mAdapter);
        viewDataBinding.setEvent(this);
    }

    private void addSticker() {
        mData = new ArrayList<>();
        mData.add(R.drawable.love_2);
        mData.add(R.drawable.main);
        mData.add(R.drawable.heoxanh_thumb);
        mData.add(R.drawable.luda_thumb);
        mData.add(R.drawable.daisyluci_thumb);
        mData.add(R.drawable.heohong_thumb);
        mData.add(R.drawable.hai);
        mData.add(R.drawable.buon);
        mData.add(R.drawable.khoc);
        mData.add(R.drawable.kiss);
        mData.add(R.drawable.main);
        mData.add(R.drawable.nen);
        mData.add(R.drawable.ok);
        mData.add(R.drawable.picachu);
    }

    @Override
    protected void startScreen() {

    }

    @Override
    protected void resumeScreen() {

    }

    @Override
    protected void pauseScreen() {

    }

    @Override
    protected void destroyScreen() {

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = getIntent();
        intent.putExtra("res", mData.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackClick() {
        onBackPressed();
    }
}
