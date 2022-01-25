package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practice138.R;

import java.util.List;

import app.app;
import interfaces.MultiAction_interface;
import models.Images_model;
import views.ShowImage_activity;

public class Images_adapter extends RecyclerView.Adapter<Images_adapter.MyViewHolder> {

    List<Images_model> list;
    Context context;
    Activity activity;

    MultiAction_interface multiAction_interface;

    public static boolean multiAction = false ;
    public static int     count = 0 ;

    public Images_adapter(List<Images_model> list, Context context, Activity activity, MultiAction_interface multiAction_interface) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.multiAction_interface = multiAction_interface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_images, parent, false) ;

        return new MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(app.BASE_URL + list.get(position).getImage()).into(holder.imageView) ;

        holder.checkbox.setVisibility(View.GONE) ;
        holder.checkbox.setChecked(false) ;
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout parent ;
        ImageView imageView ;
        CheckBox checkbox ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            parent    = itemView.findViewById(R.id.parent) ;
            imageView = itemView.findViewById(R.id.imageView) ;
            checkbox  = itemView.findViewById(R.id.checkbox) ;

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (multiAction) {
                        if (count > 0) {
                            if (!list.get(getAdapterPosition()).getMultiAction()) {
                                list.get(getAdapterPosition()).setMultiAction(true) ;
                                multiAction_interface.selected(++count, getAdapterPosition()) ;
                                checkbox.setVisibility(View.VISIBLE) ;
                                checkbox.setChecked(true) ;
                            }
                            else {
                                list.get(getAdapterPosition()).setMultiAction(false) ;
                                multiAction_interface.deSelected(--count, getAdapterPosition()) ;
                                checkbox.setVisibility(View.GONE) ;
                                checkbox.setChecked(false) ;
                            }
                        }
                        else {
                            multiAction = false ;
                        }
                    }
                    if (!multiAction) {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView, "image") ;
                        Intent intent = new Intent(context, ShowImage_activity.class) ;
                        intent.putExtra("link", list.get(getAdapterPosition()).getImage()) ;
                        context.startActivity(intent, activityOptionsCompat.toBundle()) ;
                    }
                }
            });

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (count == 0 || !multiAction) {
                        multiAction_interface.stated() ;
                        multiAction_interface.selected(++count, getAdapterPosition()) ;

                        multiAction = true ;
                        list.get(getAdapterPosition()).setMultiAction(true) ;

                        checkbox.setVisibility(View.VISIBLE) ;
                        checkbox.setChecked(true) ;
                    }
                    return true ;
                }
            });
        }
    }
}
