package com.androidgroup5.onlinecontact.stickyheader;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * The adapter to assist the {@link StickyHeaderDecoration} in creating and binding the header views.
 *
 * @param <T> the header view holder
 */
public interface StickyHeaderAdapter<T extends RecyclerView.ViewHolder> {

    /**
     * Indicate this item has no corresponding header.
     */
    long NO_HEADER = -1L;

    /**
     * Returns the header id for the item at the given position.
     *
     * @param childAdapterPosition the item adapter position
     * @return the header id, or {@link #NO_HEADER} if this item has no header.
     */
    long getHeaderId(int childAdapterPosition);

    /**
     * Creates a new header ViewHolder.
     *
     * @param parent the header's view parent, typically the RecyclerView
     * @return a view holder for the created view
     */
    T onCreateHeaderViewHolder(ViewGroup parent);

    /**
     * Display the data at the specified position.
     * <p>
     *     PLEASE NOTE THE PARAM IS CHILD POSITION!
     *     IF YOU WANT TO USE HEADER ID, PLEASE CALL YOUR {@link #getHeaderId(int)}.
     * </p>
     * @param holder the header view holder
     * @param childAdapterPosition the child item position, can be used to retrieve header id.
     */
    void onBindHeaderViewHolder(T holder, int childAdapterPosition);
}
