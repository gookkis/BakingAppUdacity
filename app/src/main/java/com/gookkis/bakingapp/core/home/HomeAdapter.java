package com.gookkis.bakingapp.core.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by herikiswanto on 8/22/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private final OnItemClickListener listener;
    private ArrayList<Recipe> data;
    private Context context;

    public HomeAdapter(Context context, ArrayList<Recipe> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
        this.context = context;
    }


    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {
        Recipe recipe = data.get(position);
        holder.click(recipe, listener);
        holder.tvName.setText(recipe.getName());
        holder.tvServings.setText(recipe.getServings() +" "+ context.getString(R.string.servings));

        if (!recipe.getImage().isEmpty()) {
            Picasso.with(context)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.place_error)
                    .into(holder.background);
        }else{
            Picasso.with(context)
                    .load(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.place_error)
                    .into(holder.background);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public interface OnItemClickListener {
        void onClick(Recipe Item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvServings;
        ImageView background;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvServings = itemView.findViewById(R.id.servings);
            background = itemView.findViewById(R.id.image);

        }


        public void click(final Recipe recipe, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(recipe);
                }
            });
        }
    }


}
