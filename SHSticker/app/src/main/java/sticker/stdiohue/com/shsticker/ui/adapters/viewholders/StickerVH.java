package sticker.stdiohue.com.shsticker.ui.adapters.viewholders;

import android.support.v7.widget.RecyclerView;

import sticker.stdiohue.com.shsticker.databinding.ItemStickerBinding;
import sticker.stdiohue.com.shsticker.eventhandlers.ItemStickerEH;
import sticker.stdiohue.com.shsticker.ui.adapters.StickerAdapter;

/**
 * Created by tran.huu.phuc(tranhuuphuc20051995@gmail.com) on 09/11/2017.
 */

public class StickerVH extends RecyclerView.ViewHolder implements ItemStickerEH {
    private ItemStickerBinding mBinding;
    private int mPosition;
    private StickerAdapter.StickerListener mListener;
    public StickerVH(ItemStickerBinding binding, StickerAdapter.StickerListener listener) {
        super(binding.getRoot());
        mBinding = binding;
        mListener = listener;
        mBinding.setEvent(this);
    }

    public void bind(Integer item, int position) {
        mPosition = position;
        mBinding.ivSticker.setImageResource(item);
    }

    @Override
    public void onItemClick() {
        mListener.onItemClick(mPosition);
    }
}
