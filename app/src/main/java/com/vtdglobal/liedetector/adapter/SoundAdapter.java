package com.vtdglobal.liedetector.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ItemSoundBinding;
import com.vtdglobal.liedetector.model.Sound;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {
    private final List<Sound> soundList;
    Context context;
    private final android.os.Handler handler = new Handler();
    private Runnable runnable;



    public interface IOnClickSoundListener { void onClickSoundListener (Sound sound);}
    IOnClickSoundListener iOnClickSoundListener;
    public SoundAdapter(Context context, List<Sound> soundList, IOnClickSoundListener iOnClickSoundListener) {
        this.context = context;
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
        holder.itemSoundBinding.layoutSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemSoundBinding.imgSoundButton.setBackgroundResource(R.drawable.bg_dark_corner_16);
                holder.itemSoundBinding.imgSoundButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this,200);
                        holder.itemSoundBinding.imgSoundButton.setBackgroundResource(R.drawable.bg_dark_heavy_corner_16);
                    }
                }, 200);
                iOnClickSoundListener.onClickSoundListener(sound);
            }
        });
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
    private void onClickButton(View myButton){
        int colorFrom = context.getResources().getColor(R.color.darkHeavy);
        int colorTo = context.getResources().getColor(R.color.darkLight);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(500); // thời gian thay đổi màu sắc
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(1);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animator) {
                myButton.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }
}
