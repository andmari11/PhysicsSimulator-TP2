package simulator.model;

import simulator.misc.Vector2D;

public class StationaryBody extends Body {

	// Constructor, se llama al del padre con v = (0,0)
	public StationaryBody(String id, String gid, Vector2D p, double masa) {

		super(id, gid, p, new Vector2D(), masa);

	}

	// advance() no hace nada, no se mueve
	@Override
	void advance(double dt) {
	}

}
