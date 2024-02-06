package simulator.factories;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	private Map<String, Builder<T>> _builders;
	private List<JSONObject> _buildersInfo;

	public BuilderBasedFactory() {
		// Create a HashMap for _builders, a LinkedList _buildersInfo
		_builders = new HashMap<>();
		_buildersInfo = new LinkedList<>();
	}

	public BuilderBasedFactory(List<Builder<T>> builders) {
		// creas los hashmaps y lista
		this();
		// addbuilder de cada builder
		for (Builder<T> b : builders) {

			addBuilder(b);
		}
	}

	@Override
	public T createInstance(JSONObject info) {

		// info es null asi q no se puede construir
		if (info == null) {
			throw new IllegalArgumentException("Invalid value for createInstance:null");
		}

		// builder no tiene type, no se puede construir
		if (!_builders.containsKey(info.getString("type"))) {

			throw new IllegalArgumentException("Invalid value for createInstance: " + info.toString());

		}

		JSONObject data;

		if (info.has("data")) {
			// info tirne data
			data = info.getJSONObject("data");
		} else {
			// al no tener info se manda un jsonboject vacio (para constructores con valores
			// default
			data = new JSONObject();
		}

		// se devuelve el builder q devuelve createInstance del constructor type
		return _builders.get(info.getString("type")).createInstance(data);

	}

	@Override
	public List<JSONObject> getInfo() {
		return Collections.unmodifiableList(_buildersInfo);
	}

	public void addBuilder(Builder<T> b) {
		// guardamos el type del constructor
		_builders.put(b.getTypeTag(), b);
		// add b.getInfo () to _buildersInfo
		_buildersInfo.add(b.getInfo());
	}

}