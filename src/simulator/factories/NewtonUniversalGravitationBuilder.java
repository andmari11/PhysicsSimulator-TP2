package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {

	// constructor para tests
	public NewtonUniversalGravitationBuilder() {

		super("nlug", "Newton Universal Gravitation ");
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) throws IllegalArgumentException {

		// data es valido
		if (data == null) {

			throw new IllegalArgumentException("Invalid value for createInstance: null");
		}

		// valor por defecto
		double G = 6.67E-11;

		// data tiene G por lo que se cambia
		if (data.has("G")) {

			G = data.getDouble("G");
		}

		return new NewtonUniversalGravitation(G);
	}

	public String getData() {
		JSONObject ret = new JSONObject();

		ret.put("G", "the gravitational constant (a number)");

		return ret.toString();

	}

	@Override
	public JSONObject getInfo() {
		JSONObject ret = new JSONObject();
		JSONObject data = new JSONObject();

		ret.put("type", "nlug");
		ret.put("desc", "Newton's law of universal gravitation");

		data.put("G", "the gravitational constant (a number)");

		ret.put("data", data);

		return ret;

	}
}