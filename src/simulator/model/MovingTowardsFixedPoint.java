package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {
	// Atributos , c es el punto hacia el cual se aplica la fuerza
	Vector2D c;
	double g = 9.81;

	// Constructores
	public MovingTowardsFixedPoint() {
	}

	public MovingTowardsFixedPoint(Vector2D c, double g) {
		// El constructor lanza excepcion en culaquiera de estos casos
		if (c == null || g <= 0) {

			throw new IllegalArgumentException("Argumento invalido, g no es positivo o c es nulo");
		}

		this.g = g;
		this.c = c;
	}

	@Override
	public void apply(List<Body> bs) {
		Vector2D f;

		for (Body b : bs) {

			f = ((c.minus(b.getPosition())).direction()).scale(g * b.getMass());
			// Se calcula la fuerza con la respectiva formula

			// Para cada cuerpo suma la fuerza
			b.addForce(f);

		}

	}

	public String toString() {

		return "Moving towards " + c + " with constant acceleration " + g;
	}
}
