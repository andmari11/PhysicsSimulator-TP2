package simulator.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;
	String[] _header = { "Id", "gId", "Mass", "Velocity", "Position", "Force" };
	List<Body> _bodies;

	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {

		return _bodies.size();
	}

	@Override
	public int getColumnCount() {

		return _header.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Body g = _bodies.get(rowIndex);
		String ret = "";

		if (columnIndex == 0) {
			ret = g.getId();
		} else if (columnIndex == 1) {
			ret = g.getgId();
		} else if (columnIndex == 2) {
			ret += g.getMass();
		} else if (columnIndex == 3) {
			ret += g.getVelocity();
		} else if (columnIndex == 4) {
			ret += g.getPosition().toString();
		} else if (columnIndex == 5) {
			ret += g.getForce().toString();
		}

		return ret;
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {

		fireTableStructureChanged();
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_bodies.clear();
		fireTableStructureChanged();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {

	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {

		// añade el body nuevo
		_bodies.add(b);
		fireTableDataChanged();

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
	}
}