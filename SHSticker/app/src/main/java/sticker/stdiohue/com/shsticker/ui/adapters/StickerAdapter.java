package sticker.stdiohue.com.shsticker.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import sticker.stdiohue.com.shsticker.databinding.ItemStickerBinding;
import sticker.stdiohue.com.shsticker.ui.adapters.viewholders.StickerVH;

/**
 * Created by tran.huu.phuc(tranhuuphuc20051995@gmail.com) on 09/11/2017.
 */

public class StickerAdapter extends RecyclerView.Adapter<StickerVH> {
    private List<Integer> mData;
    private StickerListener mListener;

    public StickerAdapter(List<Integer> data, StickerListener listener){
        mListener = listener;
        mData = data;
    }

    @Override
    public StickerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemStickerBinding itemStickerBinding = ItemStickerBinding.inflate(layoutInflater);
        return new StickerVH(itemStickerBinding, mListener);
    }

    @Override
    public void onBindViewHolder(StickerVH holder, int position) {
        holder.bind(mData.get(position), position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size() == 0 ? 0 : mData.size();
    }

    public interface StickerListener {
        void onItemClick(int position);
    }
}
