package com.gookkis.bakingapp.core.recipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gookkis.bakingapp.R;
import com.gookkis.bakingapp.model.Step;
import com.gookkis.bakingapp.utils.StepsPosition;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

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
                    /*Intent intent = new Intent(context, StepDetailActivity.class);
                    String jsonSteps = new GsonBuilder().create().toJson(data);
                    intent.putExtra(Const.STEPS, jsonSteps);
                    context.startActivity(intent);*/
                    Timber.d("Step Adapter ke Detail Activity Player");
                }
            }
        });
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
