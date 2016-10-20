package hooapps.mac.hoober1;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 10/19/16.
 */

@IgnoreExtraProperties
public class Ride {

    public String place;
    public String date;
    public String time;
    public int seats;

    public Ride() {
        // Default constructor required for calls to DataSnapshot.getValue(Ride.class)
    }

    public Ride(String place, String date, String time, int seats) {
        this.place = place;
        this.date = date;
        this.time = time;
        this.seats = seats;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("place", place);
        result.put("date", date);
        result.put("time", time);
        result.put("seats", seats);

        return result;
    }

}
