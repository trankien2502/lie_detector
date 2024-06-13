package com.vtdglobal.liedetector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vtdglobal.liedetector.databinding.ItemSoundBinding;
import com.vtdglobal.liedetector.model.Sound;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {
    private final List<Sound> soundList;


    public interface IOnClickSoundListener { void onClickSoundListener (Sound sound);}
    IOnClickSoundListener iOnClickSoundListener;
    public SoundAdapter(List<Sound> soundList, IOnClickSoundListener iOnClickSoundListener) {
        this.soundList = soundList;
        this.iOnClickSoundListener = iOnClickSoundListener;
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSoundBinding itemSoundBinding = ItemSoundBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SoundViewHolder(itemSoundBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundViewHolder holder, int position) {
        Sound sound = soundList.get(position);
        if (sound==null) return;
        holder.itemSoundBinding.imgSound.setImageResource(sound.getImage());
        holder.itemSoundBinding.tvSoundName.setText(sound.getName());
        holder.itemSoundBinding.layoutSound.setOnClickListener(view -> iOnClickSoundListener.onClickSoundListener(sound));
    }

    @Override
    public int getItemCount() {
        return soundList==null?0:soundList.size();
    }

    public static class SoundViewHolder extends RecyclerView.ViewHolder {
        private final ItemSoundBinding itemSoundBinding;

        public SoundViewHolder(ItemSoundBinding itemSoundBinding) {
            super(itemSoundBinding.getRoot());
            this.itemSoundBinding = itemSoundBinding;
        }
    }
}
