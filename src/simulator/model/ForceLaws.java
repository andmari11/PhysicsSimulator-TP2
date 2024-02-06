package simulator.model;

import java.util.List;

public interface ForceLaws {

	// Aplica fuerzas a los cuerpos a los cuerpos de la lista
	public void apply(List<Body> bs);
}
