package com.gookkis.bakingapp.core.steps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.model.Step;
import com.gookkis.bakingapp.utils.Const;
import com.gookkis.bakingapp.utils.StepsPosition;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by herikiswanto on 8/22/17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<Step> data = new ArrayList<>();
    private Context context;
    private boolean isMultiPane = false;
    private OnStepAdapterListener onStepAdapterListener;

    public StepsAdapter(Context context, ArrayList<Step> data, boolean isMultiPane, OnStepAdapterListener listener) {
        this.data = data;
        this.onStepAdapterListener = listener;
        this.isMultiPane = isMultiPane;
        this.context = context;
    }


    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_steps, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.ViewHolder holder, int position) {
        Step step = data.get(position);

        holder.tvShortDesc.setText(step.getShortDescription());
        if (!step.getThumbnailURL().isEmpty()) {
            Picasso.with(context)
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.place_error)
                    .into(holder.imgThumb);
        } else {
            Picasso.with(context)
                    .load(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.place_error)
                    .into(holder.imgThumb);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStepAdapterListener.onStepAdapterSelected(position);
                if (isMultiPane) {
                    EventBus.getDefault().post(new StepsPosition(position));
                } else {
                    Intent intent = new Intent(context, StepDescItemActivity.class);
                    intent.putExtra(Const.STEP_POS, position);
                    context.startActivity(intent);

                }
            }
        });

        if(!step.getVideoURL().isEmpty()){
            holder.imgVid.setImageResource(R.drawable.ic_slow_motion_video_blue_500_48dp);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public interface OnStepAdapterListener {
        void onStepAdapterSelected(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_short_desc)
        TextView tvShortDesc;
        @BindView(R.id.img_thumb)
        ImageView imgThumb;
        @BindView(R.id.img_vid)
        ImageView imgVid;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
