package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MovingBody;

public class MovingBodyBuilder extends Builder<Body> {

	public MovingBodyBuilder() {

		super("mv_body", "b");
	}

	@Override
	protected Body createInstance(JSONObject data) throws IllegalArgumentException {

		// data es valido y tiene todos los datos necesarios
		if (data == null || !data.has("id") || !data.has("gid") || !data.has("p") || !data.has("v") || !data.has("m")) {

			throw new IllegalArgumentException("Invalid value for createInstance: " + data.toString());
		}

		// se localizan los arrays
		JSONArray arrayp = data.getJSONArray("p");
		JSONArray arrayv = data.getJSONArray("v");

		// se comrpueba q sean 2d
		if (arrayp.length() != 2 || arrayv.length() != 2) {

			throw new IllegalArgumentException();
		}

		// se crean los vectores
		Vector2D p = new Vector2D(arrayp.getDouble(0), arrayp.getDouble(1));
		Vector2D v = new Vector2D(arrayv.getDouble(0), arrayv.getDouble(1));

		return new MovingBody(data.getString("id"), data.getString("gid"), p, v, data.getDouble("m"));
	}

}
