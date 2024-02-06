package simulator.model;

import simulator.misc.Vector2D;

public class MovingBody extends Body {

	// atributo de aceleracion
	private Vector2D a;

	// Constructor, llama al del padre
	public MovingBody(String id, String gid, Vector2D p, Vector2D v, double masa) {
		super(id, gid, p, v, masa);

	}

	@Override
	void advance(double dt) {

		// si m = 0 , aceleracion = (0,0), si no calculamos aceleracion con la segunda
		// ley de newton
		if (masa == 0.0) {

			this.a = new Vector2D();
		} else {

			this.a = this.f.scale(1 / masa);
		}

		// p=p+v*dt+0.5*a*dt^2
		this.p = p.plus(v.scale(dt).plus(a.scale(dt * dt * 0.5)));

		// v=v+a*dt
		this.v = this.v.plus(a.scale(dt));

	}

}
