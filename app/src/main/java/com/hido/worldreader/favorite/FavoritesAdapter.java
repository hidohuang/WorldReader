package com.hido.worldreader.favorite;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hido.worldreader.R;
import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;
import com.hido.worldreader.detail.DetailsActivity;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/21.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    @NonNull
    private  List<ZhihuDailyNewsQuestion> mZhiHuDailyList;
    
    @NonNull
    private final Context mContext;
    
    @NonNull
    private final LayoutInflater mLayoutInflater;
    
    public FavoritesAdapter(@NonNull Context context, @NonNull List<ZhihuDailyNewsQuestion> zhiHuDailyList) {
        mContext = context;
        mZhiHuDailyList = zhiHuDailyList;
        mLayoutInflater = LayoutInflater.from(context);
    }
    
    static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        
        View view;
        
        ImageView imageView;
        
        TextView textView;
        
        public FavoritesViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            imageView = (ImageView) view.findViewById(R.id.image_view_cover);
            textView = (TextView) view.findViewById(R.id.text_view_title);
            
            
        }
    }
    
    public void updateData(List<ZhihuDailyNewsQuestion> zhihuDailyNewsQuestions) {
        mZhiHuDailyList.clear();
        mZhiHuDailyList=zhihuDailyNewsQuestions;
        notifyDataSetChanged();
    }
    
    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FavoritesViewHolder holder = new FavoritesViewHolder(mLayoutInflater.inflate(R.layout.item_universal_layout, parent, false));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion = holder.getAdapterPosition();
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(DetailsActivity.KEY_ARTICLE_ID,mZhiHuDailyList.get(postion).getId());
                intent.putExtra(DetailsActivity.KEY_ARTICLE_TITLE, mZhiHuDailyList.get(postion).getTitle());
                intent.putExtra(DetailsActivity.KEY_ARTICLE_IS_FAVORITE, mZhiHuDailyList.get(postion).isFavorite());
                mContext.startActivity(intent);            }
        });
        return holder;
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        
        FavoritesViewHolder zivh = (FavoritesViewHolder) holder;
        ZhihuDailyNewsQuestion question = mZhiHuDailyList.get(position);
        if (question.getImages().get(0) == null) {
            zivh.imageView.setImageResource(R.drawable.photo);
        } else {
            Glide.with(mContext)
                    .load(question.getImages().get(0))
                    .asBitmap()
                    .placeholder(R.drawable.photo)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.drawable.photo)
                    .centerCrop()
                    .into(zivh.imageView);
        }
        zivh.textView.setText(question.getTitle());
    }
    
    @Override
    public int getItemCount() {
        return mZhiHuDailyList.size();
    }
}
