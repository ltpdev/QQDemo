package comemo.example.yls.qqdemo.adaper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import comemo.example.yls.qqdemo.model.SearchResultItem;
import comemo.example.yls.qqdemo.widget.SearchResultItemView;

/**
 * Created by asus- on 2017/1/2.
 */

public class AddRecyclerViewAdapter extends RecyclerView.Adapter<AddRecyclerViewAdapter.SearchResultItemViewHolder> {
    private Context context;
    private List<SearchResultItem> list;

    public AddRecyclerViewAdapter(Context context, List<SearchResultItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AddRecyclerViewAdapter.SearchResultItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultItemViewHolder(new SearchResultItemView(context));
    }

    @Override
    public void onBindViewHolder(AddRecyclerViewAdapter.SearchResultItemViewHolder holder, int position) {
        holder.searchResultItemView.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SearchResultItemViewHolder extends RecyclerView.ViewHolder {
        private SearchResultItemView searchResultItemView;

        public SearchResultItemViewHolder(SearchResultItemView searchResultItemView) {
            super(searchResultItemView);
            this.searchResultItemView = searchResultItemView;
        }
    }
}
