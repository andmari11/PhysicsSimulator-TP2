package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public abstract class Body {

	// Atributos de un cuerpo
	protected String id;
	protected String gid;
	protected Vector2D v;
	protected Vector2D p;
	protected Vector2D f;
	protected double masa;

	// Constructor
	Body(String id, String gid, Vector2D p, Vector2D v, double masa) {

		// El constructor lanza excepcion en culaquiera de estos casos
		if (id == null || gid == null || v == null || p == null || masa <= 0 || id.trim().length() <= 0
				|| gid.trim().length() <= 0) {

			throw new IllegalArgumentException("Argumento invalido para el cuerpo");
		}
		this.id = id;
		this.gid = gid;
		this.v = v;
		this.p = p;
		this.f = new Vector2D();
		this.masa = masa;
	}

	// id del cuerpo
	public String getId() {
		return this.id;
	}

	// id del grupo al que pertenece el cuerpo
	public String getgId() {
		return this.gid;
	}

	// vector velocidad
	public Vector2D getVelocity() {
		return this.v;
	}

	// vector fuerza
	public Vector2D getForce() {
		return this.f;
	}

	// vector posicion
	public Vector2D getPosition() {
		return this.p;
	}

	// masa del cuerpo
	public double getMass() {
		return this.masa;
	}

	// añade fuerza al vector fuerza del cuerpo
	void addForce(Vector2D fuerza) {
		this.f = f.plus(fuerza);
	}

	// fuerza a (0,0)
	void resetForce() {
		this.f = new Vector2D();
	}

	// Mueve el cuerpo durante dt
	abstract void advance(double dt);

	// informacion del cuerpo en formato JSON
	public JSONObject getState() {
		JSONObject body = new JSONObject();

		// JsonArray para cada atributo que sea un vector
		JSONArray ja1 = new JSONArray();

		ja1.put(p.getX());
		ja1.put(p.getY());
		body.put("p", ja1);

		JSONArray ja2 = new JSONArray();
		ja2.put(v.getX());
		ja2.put(v.getY());
		body.put("v", ja2);

		JSONArray ja3 = new JSONArray();
		ja3.put(f.getX());
		ja3.put(f.getY());
		body.put("f", ja3);

		body.put("id", this.id);
		body.put("m", this.masa);

		return body;
	}

	// Devuelve el estado del JSON a string
	public String toString() {

		return getState().toString();
	}

	public boolean equals(Body other) {

		return other.id == this.id && this.gid == other.gid;
	}
}
