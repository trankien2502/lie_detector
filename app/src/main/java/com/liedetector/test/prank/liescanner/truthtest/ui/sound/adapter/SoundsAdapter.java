package com.liedetector.test.prank.liescanner.truthtest.ui.sound.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liedetector.test.prank.liescanner.truthtest.databinding.ItemSoundsBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.model.Sound;

import java.util.List;

public class SoundsAdapter extends RecyclerView.Adapter<SoundsAdapter.SoundsViewHolder> {
    private final List<Sound> soundList;
    Context context;
    private final Handler handler = new Handler();
    private Runnable runnable;


    public interface IOnClickSoundListener {
        void onClickSoundListener(Sound sound);
    }

    IOnClickSoundListener iOnClickSoundListener;

    public SoundsAdapter(Context context, List<Sound> soundList, IOnClickSoundListener iOnClickSoundListener) {
        this.context = context;
        this.soundList = soundList;
        this.iOnClickSoundListener = iOnClickSoundListener;
    }

    @NonNull
    @Override
    public SoundsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSoundsBinding itemSoundsBinding = ItemSoundsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SoundsViewHolder(itemSoundsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundsViewHolder holder, int position) {
        Sound sound = soundList.get(position);
        if (sound == null) return;
        holder.itemSoundsBinding.imgSound.setImageResource(sound.getImage());
        holder.itemSoundsBinding.tvSoundName.setText(sound.getName());
        holder.itemSoundsBinding.layoutSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClickSoundListener.onClickSoundListener(sound);
            }
        });
    }

    @Override
    public int getItemCount() {
        return soundList == null ? 0 : soundList.size();
    }

    public static class SoundsViewHolder extends RecyclerView.ViewHolder {
        private final ItemSoundsBinding itemSoundsBinding;

        public SoundsViewHolder(ItemSoundsBinding itemSoundsBinding) {
            super(itemSoundsBinding.getRoot());
            this.itemSoundsBinding = itemSoundsBinding;
        }
    }

}
