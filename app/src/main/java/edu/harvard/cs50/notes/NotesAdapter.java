package edu.harvard.cs50.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public CardView containerView;
        public TextView nameTextView;

        public NoteViewHolder(View view) {
            super(view);
            this.containerView = view.findViewById(R.id.card_view);
            this.nameTextView = view.findViewById(R.id.note_row_name);

            this.containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Note note = (Note) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), NoteActivity.class);
                    intent.putExtra("id", note.id);
                    intent.putExtra("content", note.content);

                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Note> notes = new ArrayList<>();

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note current = notes.get(position);
        holder.containerView.setTag(current);
        if (! current.content.isEmpty())
            holder.nameTextView.setText(current.content);
        else
            holder.nameTextView.setText("<empty note>");
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void reload() {
        notes = MainActivity.database.noteDao().getAll();
        notifyDataSetChanged();
    }

    public Note getNote(int position) {
        return notes.get(position);
    }

    public void removeItem(int position, long id) {
        notes.remove(position);
        MainActivity.database.noteDao().delete(id);
        notifyItemRemoved(position);
    }

    public void restoreItem(Note item, int position) {
        notes.add(position, item);
        MainActivity.database.noteDao().create(item.content);
        notifyItemInserted(position);
    }
}