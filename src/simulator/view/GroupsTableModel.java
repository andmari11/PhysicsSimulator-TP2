package simulator.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class GroupsTableModel extends AbstractTableModel implements SimulatorObserver {
	String[] _header = { "Id", "Force Laws", "Bodies" };
	List<BodiesGroup> _groups;
	Controller _ctrl;

	GroupsTableModel(Controller ctrl) {
		_groups = new ArrayList<>();
		ctrl.addObserver(this);
		_ctrl = ctrl;
	}

	@Override
	public int getRowCount() {
		return _groups.size();
	}

	@Override
	public int getColumnCount() {
		return _header.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		BodiesGroup g = _groups.get(rowIndex);
		String ret = "";

		switch (columnIndex) {

		case 0:
			ret = g.getId();
			break;

		case 1:
			ret = g.getForceLawsInfo();
			break;

		case 2: {

			for (Body b : g.getListaRO()) {

				ret += b.getId() + " ";
			}
		}
			break;
		}
		return ret;
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		fireTableStructureChanged();
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {

		_groups.clear();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {

		_groups.add(g);
		fireTableDataChanged();

	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		fireTableDataChanged();

	}
}
