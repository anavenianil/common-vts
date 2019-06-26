

package com.affluencesystems.insurancetelematics.common.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ShimmerAdapter extends RecyclerView.Adapter<ShimmerViewHolder> {

    private int mItemCount;
    private int mLayoutReference;
    private int mShimmerAngle;
    private int mShimmerColor;
    private int mShimmerDuration;
    private float mShimmerMaskWidth;
    private boolean isAnimationReversed;
    private Drawable mShimmerItemBackground;

    @Override
    public ShimmerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ShimmerViewHolder shimmerViewHolder = new ShimmerViewHolder(inflater, parent, mLayoutReference);
        shimmerViewHolder.setShimmerColor(mShimmerColor);
        shimmerViewHolder.setShimmerAngle(mShimmerAngle);
        shimmerViewHolder.setShimmerMaskWidth(mShimmerMaskWidth);
        shimmerViewHolder.setShimmerViewHolderBackground(mShimmerItemBackground);
        shimmerViewHolder.setShimmerAnimationDuration(mShimmerDuration);
        shimmerViewHolder.setAnimationReversed(isAnimationReversed);

        return shimmerViewHolder;
    }

    @Override
    public void onBindViewHolder(ShimmerViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    /*
    *       set minimum count.
    * */
    public void setMinItemCount(int itemCount) {
        mItemCount = itemCount;
    }

    /*
    *       set shimmer angle.
    * */
    public void setShimmerAngle(int shimmerAngle) {
        this.mShimmerAngle = shimmerAngle;
    }

    /*
    *       set shimmer color.
    * */
    public void setShimmerColor(int shimmerColor) {
        this.mShimmerColor = shimmerColor;
    }

    /*
    *       set shimmer width.
    * */
    public void setShimmerMaskWidth(float maskWidth) {
        this.mShimmerMaskWidth = maskWidth;
    }

    /*
    *       shimmer background.
    * */
    public void setShimmerItemBackground(Drawable shimmerItemBackground) {
        this.mShimmerItemBackground = shimmerItemBackground;
    }

    /*
    *       set shimmer duration
    * */
    public void setShimmerDuration(int mShimmerDuration) {
        this.mShimmerDuration = mShimmerDuration;
    }

    /*
    *       set layout
    * */
    public void setLayoutReference(int layoutReference) {
        this.mLayoutReference = layoutReference;
    }

    /*
    *       if need animation reverse.
    * */
    public void setAnimationReversed(boolean animationReversed) {
        this.isAnimationReversed = animationReversed;
    }
}
