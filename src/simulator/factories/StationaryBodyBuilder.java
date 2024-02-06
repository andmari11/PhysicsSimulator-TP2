package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.StationaryBody;

public class StationaryBodyBuilder extends Builder<Body> {

	public StationaryBodyBuilder() {
		super("st_body", "a");
	}

	@Override
	protected Body createInstance(JSONObject data) throws IllegalArgumentException {

		// data es valido
		if (data == null || !data.has("id") || !data.has("gid") || !data.has("p") || !data.has("m")) {

			throw new IllegalArgumentException();
		}

		// se localizan los arrays
		JSONArray arrayp = data.getJSONArray("p");

		// se comrpueba q sean 2d
		if (arrayp.length() != 2) {

			throw new IllegalArgumentException();
		}

		// se crean los vectores
		Vector2D p = new Vector2D(arrayp.getDouble(0), arrayp.getDouble(1));

		return new StationaryBody(data.getString("id"), data.getString("gid"), p, data.getDouble("m"));
	}

}