package com.vtdglobal.liedetector.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ItemLanguageBinding;
import com.vtdglobal.liedetector.model.Language;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {
    private int selectedPosition = -1;
    private final List<Language> listLanguage;
    private final Context context;
    private boolean isSettingMode = false;

    public interface IOnClickLanguage { void onClickLanguageListener (Language language);}
    IOnClickLanguage iOnClickLanguage;
    public LanguageAdapter(Context context, List<Language> listLanguage, IOnClickLanguage iOnClickLanguage) {
        this.context = context;
        this.listLanguage = listLanguage;
        this.iOnClickLanguage = iOnClickLanguage;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLanguageBinding itemLanguageBinding = ItemLanguageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new LanguageViewHolder(itemLanguageBinding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        Language language = listLanguage.get(position);
        if (language==null) return;
        Typeface fontLanguageSetting = Typeface.createFromAsset(context.getAssets(), "font/druk_text_wide_medium_trial.otf");
        holder.itemLanguageBinding.imgLanguage.setImageResource(language.getImage());
        holder.itemLanguageBinding.tvNameLanguage.setText(language.getName());
        if (isSettingMode) holder.itemLanguageBinding.tvNameLanguage.setTypeface(fontLanguageSetting);
        holder.itemLanguageBinding.layoutItemLanguage.setOnClickListener(view -> iOnClickLanguage.onClickLanguageListener(language));
        if (position == selectedPosition){
            holder.itemLanguageBinding.tvNameLanguage.setTextColor(context.getResources().getColor(R.color.blueLight));
            holder.itemLanguageBinding.viewForeground.setVisibility(View.VISIBLE);
            holder.itemLanguageBinding.layoutItemLanguage.setBackgroundResource(R.drawable.bg_darklight_blue_border_corner_8);
        } else {
            holder.itemLanguageBinding.tvNameLanguage.setTextColor(context.getResources().getColor(R.color.white));
            holder.itemLanguageBinding.viewForeground.setVisibility(View.GONE);
            holder.itemLanguageBinding.layoutItemLanguage.setBackgroundResource(R.drawable.bg_darklight_corner_8);
        }
    }

    @Override
    public int getItemCount() {
        return null == listLanguage ? 0 : listLanguage.size();
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        private final ItemLanguageBinding itemLanguageBinding;
        public LanguageViewHolder(ItemLanguageBinding itemLanguageBinding) {
            super(itemLanguageBinding.getRoot());
            this.itemLanguageBinding = itemLanguageBinding;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public void setSettingMode(boolean settingMode) {
        isSettingMode = settingMode;
    }
}
