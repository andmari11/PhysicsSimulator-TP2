package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

	// constructor para tests
	public MovingTowardsFixedPointBuilder() {

		super("mtfp", "Moving towards a fixed point");
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) throws IllegalArgumentException {

		// data es valido
		if (data == null) {

			throw new IllegalArgumentException("Invalid value for createInstance: null");
		}

		Vector2D c;
		// si en data no hay c hay q poner el vector 0, 0
		if (data.has("c")) {

			// se localiza el array
			JSONArray arrayC = data.getJSONArray("c");

			// se colocan los valores en el vector
			c = new Vector2D(arrayC.getDouble(0), arrayC.getDouble(1));
		}
		// sino vectr 0 0
		else {
			c = new Vector2D();
		}

		// si no hay g en data se pone 9.81
		double g = 9.81;
		// si en data nos dan g, se pone esa g
		if (data.has("g")) {

			g = data.getDouble("g");
		}

		return new MovingTowardsFixedPoint(c, g);

	}

	public String getData() {
		JSONObject ret = new JSONObject();

		ret.put("c", "the point towards which bodies move (e.g., [100.0,50.0])");
		ret.put("g", "the length of the acceleration vector (a number)");

		return ret.toString();

	}

	@Override
	public JSONObject getInfo() {
		JSONObject ret = new JSONObject();
		JSONObject data = new JSONObject();

		ret.put("type", "mtfp");
		ret.put("desc", "Moving towards a fixed point");

		data.put("c", "the point towards which bodies move (e.g., [100.0,50.0])");
		data.put("g", "the length of the acceleration vector (a number)");

		ret.put("data", data);

		return ret;
	}
}