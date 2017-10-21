package com.hido.worldreader.article;

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
 * Created by hidohuang on 2017/9/26.
 */

public class ZhihuDailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    
    private final List<ZhihuDailyNewsQuestion> mZhihuDailyNewsQuestions;
    
    @NonNull
    private final Context mContext;
    
    @NonNull
    private final LayoutInflater mLayoutInflater;
    
    public ZhihuDailyAdapter(@NonNull Context context, @NonNull List<ZhihuDailyNewsQuestion> zhihuDailyNewsQuestions) {
        mContext = context;
        mZhihuDailyNewsQuestions = zhihuDailyNewsQuestions;
        mLayoutInflater = LayoutInflater.from(context);
        
    }
    
    static class ZhihuViewHolder extends RecyclerView.ViewHolder {
        
        View view;
        
        ImageView imageView;
        
        TextView textView;
        
        public ZhihuViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            imageView = view.findViewById(R.id.image_view_cover);
            textView = view.findViewById(R.id.text_view_title);
            
            
        }
    }
    
    public void updateData(List<ZhihuDailyNewsQuestion> list) {
        mZhihuDailyNewsQuestions.clear();
        mZhihuDailyNewsQuestions.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }
    
    @Override
    public ZhihuDailyAdapter.ZhihuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ZhihuDailyAdapter.ZhihuViewHolder holder = new ZhihuDailyAdapter.ZhihuViewHolder(mLayoutInflater.inflate(R.layout.item_universal_layout, parent, false));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion = holder.getAdapterPosition();
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(DetailsActivity.KEY_ARTICLE_ID,mZhihuDailyNewsQuestions.get(postion).getId());
                intent.putExtra(DetailsActivity.KEY_ARTICLE_TITLE, mZhihuDailyNewsQuestions.get(postion).getTitle());
                intent.putExtra(DetailsActivity.KEY_ARTICLE_IS_FAVORITE, mZhihuDailyNewsQuestions.get(postion).isFavorite());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        
        ZhihuDailyNewsQuestion item = mZhihuDailyNewsQuestions.get(position);
        ZhihuViewHolder viewHolder = (ZhihuViewHolder) holder;
        
        if (item.getImages().get(0) == null) {
            viewHolder.imageView.setImageResource(R.drawable.photo);
        } else {
            Glide.with(mContext).load(item.getImages().get(0)).asBitmap().placeholder(R.drawable.photo).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.photo).centerCrop().into(viewHolder.imageView);
        }
        viewHolder.textView.setText(item.getTitle());
    }
    
    @Override
    public int getItemCount() {
        return mZhihuDailyNewsQuestions.isEmpty() ? 0 : mZhihuDailyNewsQuestions.size();
    }
}
