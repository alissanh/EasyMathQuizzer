package persistence;

import org.json.JSONObject;

public interface Writable {

    // FROM JsonSerializationDemo by Paul Carter
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
