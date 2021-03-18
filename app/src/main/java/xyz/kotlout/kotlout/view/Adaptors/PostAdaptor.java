package xyz.kotlout.kotlout.view.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Recycler;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.List;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.experiment.Post;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.ViewHolder> {

  private List<Post> postData;

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_post_comment,
        parent, false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      Post post = postData.get(position);
      holder.getName().setText(post.getText());
      holder.getName().setText(post.getPoster());
  }

  @Override
  public int getItemCount() {
    return postData.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView date;
    TextView text;
    TextView replies;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      name = itemView.findViewById(R.id.comment_name);
      date = itemView.findViewById(R.id.comment_date);
      text = itemView.findViewById(R.id.comment_text);
      replies = itemView.findViewById(R.id.comment_reply_count);
    }

    public TextView getName() {
      return name;
    }

    public TextView getDate() {
      return date;
    }

    public TextView getText() {
      return text;
    }

    public TextView getReplies() {
      return replies;
    }
  }

  public PostAdaptor(List<Post> posts) {
    this.postData = posts;
  }

}