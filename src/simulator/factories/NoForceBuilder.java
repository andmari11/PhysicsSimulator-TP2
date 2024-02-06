package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {

	public NoForceBuilder() {
		super("nf", "No Force");
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) throws IllegalArgumentException {

		// data es valido
		if (data == null) {

			throw new IllegalArgumentException();
		}

		return new NoForce();
	}

	public String getData() {

		return "";
	}

	@Override
	public JSONObject getInfo() {

		JSONObject jBody = new JSONObject();
		JSONObject jData = new JSONObject();

		jBody.put("type", "nf");
		jBody.put("desc", "No force");
		jBody.put("data", jData);

		return jBody;
	}
}