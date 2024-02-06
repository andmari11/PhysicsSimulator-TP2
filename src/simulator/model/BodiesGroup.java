package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BodiesGroup {

	// Atributos de un body group
	private String id;
	private ForceLaws fl;
	private List<Body> lista;
	private List<Body> listaRO;

	// Constructor
	BodiesGroup(String id, ForceLaws fl) {

		// El constructor lanza excepcion en culaquiera de estos casos

		if (id.trim().length() == 0 || id == null || fl == null) {

			throw new IllegalArgumentException("Argumento invalido para el body group");
		}

		this.id = id;
		this.fl = fl;
		lista = new ArrayList<>();
		// _bodiesRO.iterator()
		listaRO = Collections.unmodifiableList(lista);
	}

	// id del grupo
	public String getId() {
		return id;
	};

	// a�ade el cuerpo b a la lista de cuerpos
	void addBody(Body b) {

		if (b == null) {

			throw new IllegalArgumentException("Group no puede ser null");

		}

		// Si existe cuerpo con el mismo identificador o b es null lanza excepcion
		for (Body c : lista) {
			if (c.id.equals(b.getId())) {

				throw new IllegalArgumentException("Id " + b.getId() + ", cuerpo no valido");
			}

		}

		// a�ade
		lista.add(b);
	}

	// Cambia las leyes de fuerza
	void setForceLaws(ForceLaws fl) {

		// Excepcion si fl = null
		if (fl == null) {

			throw new IllegalArgumentException("Ley de fuerza " + fl + " invalida");

		}

		this.fl = fl;
	}

	// aplica un paso de simulaci�n en el grupo
	void advance(double dt) {

		// excepcion si dt no es positivo
		if (dt <= 0) {

			throw new IllegalArgumentException("dt = " + dt + " no es positivo");

		}

		// Llama a resetForce() de todos los cuerpos
		for (Body b : lista) {

			b.resetForce();
		}

		// Apilca las leyes de fuerza
		fl.apply(lista);

		// llama a advance(dt) para cada cuerpo,
		for (Body b : lista) {

			b.advance(dt);
		}
	}

	// Devuelve el estado del grupo en formato JSON
	public JSONObject getState() {

		JSONObject bodyGroup = new JSONObject();

		bodyGroup.put("id", this.id);

		JSONArray ja1 = new JSONArray();
		for (Body b : lista) {

			ja1.put(b.getState());
		}

		bodyGroup.put("bodies", ja1);

		return bodyGroup;

	}

	// a String
	public String toString() {

		return getState().toString();
	}

	public String getForceLawsInfo() {

		return fl.toString();
	}

	public List<Body> getListaRO() {

		return this.listaRO;
	}

}
