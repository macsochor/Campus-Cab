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

    public String origin;
    public String destination;
    public String date;
    public String time;
    public int seats;
    public boolean isPassenger;

    public Ride() {
        // Default constructor required for calls to DataSnapshot.getValue(Ride.class)
    }

    public Ride(String origin, String destinaiton, String date, String time, int seats, boolean isPassenger) {
        this.origin = origin;
        this.destination = destinaiton;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.isPassenger = isPassenger;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("origin", origin);
        result.put("destination", destination);
        result.put("date", date);
        result.put("time", time);
        result.put("seats", seats);
        result.put("isPassenger", isPassenger);
        return result;
    }

}
