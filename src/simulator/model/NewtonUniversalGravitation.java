package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {

	// Atributo g
	private double g = 6.67E-11;

	// Constructores
	public NewtonUniversalGravitation() {
	}

	public NewtonUniversalGravitation(double g) {

		// Si g no es positivo excepcion
		if (g <= 0) {

			throw new IllegalArgumentException("Argumento invalido para g = " + g);
		}

		this.g = g;
	}

	@Override
	public void apply(List<Body> bs) {
		Vector2D f;
		double fMod;

		// dos cuerpos aplican una fuerza gravitacional uno sobre otro
		for (Body j : bs) {

			for (Body i : bs) {

				// Si la distancia no es 0
				if (j.p.distanceTo(i.p) != 0) {
					fMod = g * (i.getMass() * j.getMass() / Math.pow(j.getPosition().distanceTo(i.getPosition()), 2));

					f = (j.getPosition().minus(i.getPosition()).scale(1 / i.getPosition().distanceTo(j.getPosition())))
							.scale(fMod);

//	ye=(body1.vPosicion.minus(body.vPosicion)).scale(1/body.vPosicion.distanceTo(body1.getPosition())).scale(f);
					// Fuerza que genera un cuerpo sobre el otro
					// f=g*(i.getMass()*j.getMass()/Math.pow(j.p.distanceTo(i.p), 2));

					// Calcular la direccion de fuerza
					// fx=((j.p.getX()-i.p.getX())/j.p.distanceTo(i.getPosition()))*f;
					// fy=((j.p.getY()-i.p.getY())/j.p.distanceTo(i.getPosition()))*f;

				} else {

					f = new Vector2D();
				}

				// Sumamos la fuerza
				i.addForce(f);

			}
		}
	}

	public String toString() {

		return "Newton�s Universal Gravitation with G=" + g;
	}
}
