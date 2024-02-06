package simulator.model;

import java.util.List;

public class NoForce implements ForceLaws {

	// Constructor
	public NoForce() {
	}

	// Los cuerpos se mueven con aceleracion nula
	@Override
	public void apply(List<Body> bs) {
	}

	public String toString() {

		return "No Force";
	}
}
