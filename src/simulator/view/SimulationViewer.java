package simulator.view;

import javax.swing.JComponent;

import simulator.model.BodiesGroup;
import simulator.model.Body;

@SuppressWarnings("serial")
public abstract class SimulationViewer extends JComponent {

	public abstract void addGroup(BodiesGroup g);

	public abstract void addBody(Body b);

	public abstract void update();

	public abstract void reset();
}
