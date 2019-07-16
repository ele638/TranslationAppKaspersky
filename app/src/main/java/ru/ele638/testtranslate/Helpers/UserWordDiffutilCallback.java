package ru.ele638.testtranslate.Helpers;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

import ru.ele638.testtranslate.Models.UserWord;

public class UserWordDiffutilCallback extends DiffUtil.Callback {

    private ArrayList<UserWord> oldList;
    private ArrayList<UserWord> newList;

    public UserWordDiffutilCallback(ArrayList<UserWord> iOldList, ArrayList<UserWord> iNewList) {
        oldList = iOldList;
        newList = iNewList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
