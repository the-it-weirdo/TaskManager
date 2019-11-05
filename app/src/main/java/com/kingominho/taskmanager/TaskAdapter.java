package com.kingominho.taskmanager;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskHolder> {

    private OnTaskInteractionListener mListener;

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    public Task getTaskAt(int position) {
        return getItem(position);
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            boolean isSame = false;
            if (oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getCategory().equals(newItem.getCategory()) &&
                    oldItem.getUserId() == newItem.getUserId() &&
                    oldItem.getIsCompleted() == newItem.getIsCompleted()) {
                isSame = true;
            }
            return isSame;
        }
    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentItem = getItem(position);

        boolean isCompleted = currentItem.getIsCompleted() == 1;

        holder.textViewTaskTitle.setText(currentItem.getTitle());
        if (isCompleted) {
            holder.textViewTaskTitle.setPaintFlags(holder.textViewTaskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewTaskTitle.setPaintFlags(holder.textViewTaskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.checkBoxTaskIsCompleted.setChecked(isCompleted);
    }

    class TaskHolder extends RecyclerView.ViewHolder {

        private TextView textViewTaskTitle;
        private CheckBox checkBoxTaskIsCompleted;
        private ImageButton imageButtonTaskDeleteButton;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            textViewTaskTitle = itemView.findViewById(R.id.task_title_text_view);
            checkBoxTaskIsCompleted = itemView.findViewById(R.id.task_checkbox);
            imageButtonTaskDeleteButton = itemView.findViewById(R.id.task_delete_button);

            imageButtonTaskDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (mListener != null && position != RecyclerView.NO_POSITION)
                        mListener.OnDeleteButtonPressed(getItem(position));
                }
            });

            checkBoxTaskIsCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (mListener != null && position != RecyclerView.NO_POSITION)
                        mListener.OnCheckBoxChanged(getItem(position), checkBoxTaskIsCompleted.isChecked());
                }
            });
        }
    }

    public interface OnTaskInteractionListener {
        void OnDeleteButtonPressed(Task task);

        void OnCheckBoxChanged(Task task, boolean isChecked);
    }

    public void setOnTaskInteractionListener(OnTaskInteractionListener mListener) {
        this.mListener = mListener;
    }
}
