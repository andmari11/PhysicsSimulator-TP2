package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator implements Observable<SimulatorObserver> {

	// Atributos del simulador
	private double dt;

	// Tiempo actual
	private double t = 0;

	private ForceLaws fl;

	// Mapa con clave id del grupo y valor el grupo
	private Map<String, BodiesGroup> map;
	private Map<String, BodiesGroup> mapRO;

	private List<String> listaId;

	private List<SimulatorObserver> listaObservers;

	// Constructor
	public PhysicsSimulator(ForceLaws fl, double dt) {
		// El constructor lanza excepcion en culaquiera de estos casos

		if (fl == null || dt < 0) {

			throw new IllegalArgumentException("Argumento invalido para el simulador");

		}

		this.fl = fl;
		this.dt = dt;
		map = new HashMap<>();
		mapRO = Collections.unmodifiableMap(map);
		listaId = new ArrayList<>();
		listaObservers = new ArrayList<>();
	}

	public void advance() {

		// recorremos el hashmap

		BodiesGroup b;

		for (String id : listaId) {

			b = map.get(id);
			b.advance(dt);
		}

		// Se incrementa el tiempo actual
		t += dt;

		for (SimulatorObserver o : listaObservers) {

			o.onAdvance(map, t);
		}
	}

	// a�ade un nuevo grupo con identificador id al mapa de grupos
	public void addGroup(String id) throws IllegalArgumentException {

		if (map.containsKey(id)) {

			throw new IllegalArgumentException("Identificador" + id + " de grupo no valido");

		}

		// Se crea el grupo
		BodiesGroup bg = null;
		bg = new BodiesGroup(id, this.fl);

		// Se a�ade al mapa y a la lista
		map.put(id, bg);
		listaId.add(id);

		for (SimulatorObserver o : listaObservers) {

			o.onGroupAdded(mapRO, bg);
		}

	}

	// a�ade el cuerpo b al grupo
	public void addBody(Body b) {

		// Se a�ade con el identificador b.getgId()
		if (map.containsKey(b.getgId())) {

			map.get(b.getgId()).addBody(b);
		} else {

			throw new IllegalArgumentException("No existe grupo con dicho identificador: " + b.getgId());

		}

		for (SimulatorObserver o : listaObservers) {

			o.onBodyAdded(mapRO, b);
		}
	}

	// cambia las leyes de fuerza del grupo con id a fl
	public void setForceLaws(String id, ForceLaws fl) {
		BodiesGroup bg;

		// Si encuentra id cambia las leyes de fuerza
		if (map.containsKey(id)) {

			bg = map.get(id);
			bg.setForceLaws(fl);
		}

		// Si no lo encuentra el id lanza excepcion
		else {

			throw new IllegalArgumentException("No existe grupo con id: " + id);

		}

		for (SimulatorObserver o : listaObservers) {

			o.onForceLawsChanged(bg);
		}
	}

	// Devuelve el estado del simulador fisico en formato JSON
	public JSONObject getState() {

		JSONObject ret = new JSONObject();

		// a�adimos time
		ret.put("time", this.t);

		// creamos el array de grupos
		JSONArray ja1 = new JSONArray();

		// recorremos el hashmap

		BodiesGroup b;

		for (String id : listaId) {

			b = map.get(id);

			ja1.put(b.getState());
		}

		ret.put("groups", ja1);

		return ret;
	}

	// a String
	public String toString() {
		return getState().toString();
	}

	public void setDeltaTime(double dt) {

		this.dt = dt;

		for (SimulatorObserver o : listaObservers) {

			o.onDeltaTimeChanged(dt);
		}
	}

	public void reset() {
		map.clear();
		listaId.clear();
		this.t = 0.0;

		for (SimulatorObserver o : listaObservers) {

			o.onReset(mapRO, t, dt);
		}

	}

	@Override
	public void addObserver(SimulatorObserver o) {
		o.onRegister(mapRO, t, dt);
		this.listaObservers.add(o);
	}

	@Override
	public void removeObserver(SimulatorObserver o) {

		listaObservers.remove(o);
	}
	

	public double getDeltaTime() {
		return dt;
	}

	public Map<String, BodiesGroup> getGroupsList() {
		return mapRO;
	}

	public List<String> getGroupIdList() {
		return listaId;
	}


}
