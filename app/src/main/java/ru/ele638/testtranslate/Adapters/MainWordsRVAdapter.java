package ru.ele638.testtranslate.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.ele638.testtranslate.Helpers.UserWordDiffutilCallback;
import ru.ele638.testtranslate.Models.UserWord;
import ru.ele638.testtranslate.R;

public class MainWordsRVAdapter extends RecyclerView.Adapter<MainWordsRVAdapter.ViewHolder> implements Filterable {

    private ArrayList<UserWord> userWords = new ArrayList<>();
    private ArrayList<UserWord> userWordsFiltered = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_word_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserWord word = userWordsFiltered.get(position);
        holder.userWordText.setText(word.getTextForCard());
        if (word.isEmptyTranslation()) {
            holder.loadingLayout.setVisibility(View.VISIBLE);
            holder.translationsText.setVisibility(View.GONE);
        } else {
            holder.loadingLayout.setVisibility(View.GONE);
            holder.translationsText.setVisibility(View.VISIBLE);
            holder.translationsText.setText(word.getTranslation());
        }
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView userWordText, translationsText;
        LinearLayout loadingLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userWordText = itemView.findViewById(R.id.user_word_cardview_text);
            translationsText = itemView.findViewById(R.id.user_word_cardview_translations);
            loadingLayout = itemView.findViewById(R.id.user_word_cardview_loading_layout);
        }
    }
}
