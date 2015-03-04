package udacity.gas.com.solveaproblem.drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import udacity.gas.com.solveaproblem.R;

/**
 * Created by Fagbohungbe on 20/02/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    private LayoutInflater inflater;
    List<DrawerList> drawerLists = Collections.emptyList();

    public DrawerAdapter(Context context, List<DrawerList> drawerLists) {
        inflater = LayoutInflater.from(context);
        this.drawerLists = drawerLists;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawer_layout, parent, false);
        return new DrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        DrawerList drawerList = this.drawerLists.get(position);
        holder.title.setText(drawerList.title);
        holder.icon.setImageResource(drawerList.iconId);
    }

    @Override
    public int getItemCount() {
        return drawerLists.size();
    }

    class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.drawer_text);
            icon = (ImageView) itemView.findViewById(R.id.drawer_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Use the getPosition(); to determine the position and launch the
            //respective activity
            Toast.makeText(v.getContext(), "cLICKED", Toast.LENGTH_SHORT).show();
        }
    }
}
