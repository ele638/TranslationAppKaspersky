package ru.ele638.testtranslate.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.ele638.testtranslate.Helpers.OnWordInterractionListener;
import ru.ele638.testtranslate.Helpers.UserWordDiffutilCallback;
import ru.ele638.testtranslate.Models.UserWord;
import ru.ele638.testtranslate.R;

public class MainWordsRVAdapter extends RecyclerView.Adapter<MainWordsRVAdapter.ViewHolder> implements Filterable {

    private ArrayList<UserWord> userWords = new ArrayList<>();
    private ArrayList<UserWord> userWordsFiltered = new ArrayList<>();
    private OnWordInterractionListener listener;
    private boolean deletionMode;

    public void setListener(OnWordInterractionListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_word_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UserWord word = userWordsFiltered.get(position);
        holder.userWordText.setText(word.getTextForCard());
        if (word.isEmptyTranslation()) {
            holder.loadingLayout.setVisibility(View.VISIBLE);
            holder.translationsText.setVisibility(View.GONE);
        } else {
            holder.loadingLayout.setVisibility(View.GONE);
            holder.translationsText.setVisibility(View.VISIBLE);
            holder.translationsText.setText(word.getTranslation());
        }
        holder.setFavBtnState(word.getIsFavorite() == 1);

        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                word.switchIsFavorite();
                holder.setFavBtnState(word.getIsFavorite() == 1);
                listener.updateWord(word);
            }
        });

        holder.delBtn.setVisibility(deletionMode ? View.VISIBLE : View.GONE);
        holder.favBtn.setVisibility(deletionMode ? View.GONE : View.VISIBLE);

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deletionMode){
                    listener.deleteWord(word);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userWordsFiltered.size();
    }

    public boolean isEmpty() {
        return userWords.size() == 0;
    }

    public void setData(ArrayList<UserWord> iUserWords) {
        if (iUserWords != null) {
            userWords = iUserWords;
            userWordsFiltered = iUserWords;
        }
    }

    @Override
    public Filter getFilter() {
        final MainWordsRVAdapter adapter = this;
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if (charSequence == null || charSequence.toString().isEmpty()) {
                    results.values = userWords;
                } else {
                    ArrayList<UserWord> filteredList = new ArrayList<>();
                    for (int i = 0; i < userWords.size(); i++) {
                        if (userWords.get(i).getText().contains(charSequence) ||
                                userWords.get(i).getTranslateWord().contains(charSequence))
                            filteredList.add(userWords.get(i));
                    }
                    results.values = filteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ArrayList<UserWord> results = (ArrayList<UserWord>) filterResults.values;
                UserWordDiffutilCallback callback = new UserWordDiffutilCallback(userWordsFiltered, results);
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
                userWordsFiltered = results;
                diffResult.dispatchUpdatesTo(adapter);
            }
        };
    }

    public void setDeletionMode(boolean deletionMode) {
        this.deletionMode = deletionMode;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView userWordText, translationsText;
        LinearLayout loadingLayout;
        ImageButton favBtn, delBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userWordText = itemView.findViewById(R.id.user_word_cardview_text);
            translationsText = itemView.findViewById(R.id.user_word_cardview_translations);
            loadingLayout = itemView.findViewById(R.id.user_word_cardview_loading_layout);
            favBtn = itemView.findViewById(R.id.user_word_cardview_fav_toggle);
            delBtn = itemView.findViewById(R.id.user_word_delete_btn);
        }

        public void setFavBtnState(boolean state){
            if (state){
                favBtn.setImageResource(R.drawable.fav_on);
            } else {
                favBtn.setImageResource(R.drawable.fav_off);
            }
        }
    }
}
