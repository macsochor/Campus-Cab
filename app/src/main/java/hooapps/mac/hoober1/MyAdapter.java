package hooapps.mac.hoober1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Ride> data;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView origintv, desttv, datetv, timetv, agetv;
        public Button delbtn;
        public ViewHolder(View v) {
            super(v);
            delbtn = (Button) v.findViewById(R.id.del_button);
            desttv = (TextView)v.findViewById(R.id.destinationTV);
            origintv = (TextView)v.findViewById(R.id.originTV);
            datetv = (TextView)v.findViewById(R.id.dateTV);
            timetv = (TextView)v.findViewById(R.id.timeTV);
            agetv = (TextView)v.findViewById(R.id.ageTV);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Ride> myDataset) {
        data = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_obj_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new MyAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Ride ride = data.get(position);
        holder.desttv.setText(ride.destination);
        holder.origintv.setText(ride.origin);
        holder.agetv.setText(ride.timeCreated);
        holder.timetv.setText(ride.time);
        holder.datetv.setText(ride.date);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}