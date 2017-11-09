package sticker.stdiohue.com.shsticker.ui.bases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by tran.huu.phuc on 11/18/16.
 */

public abstract class AbsListBindingAdapter<T extends ViewDataBinding> extends ArrayAdapter<T> {
    @LayoutRes
    public abstract int getLayoutResource();

    public abstract void bind(T binding, int position);

    public abstract int getSize();

    protected AbsListBindingAdapter(@NonNull Context context) {
        super(context, 0);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") T binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutResource(), parent, false);
        bind(binding, position);
        return binding.getRoot();
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        T binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutResource(), parent, false);
        bind(binding, position);
        return binding.getRoot();
    }

    @Override
    public int getCount() {
        return getSize();
    }

}