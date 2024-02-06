package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class StatusBar extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;
	private int _nGrupos = 0;
	private double _time = 0;

	JLabel nGruposLabel;
	JLabel tiempoLabel;

	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));

		// etiqueta tiempo
		tiempoLabel = new JLabel("Time:" + _time);
		this.add(tiempoLabel);

		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);

		// etiqueta num grupos
		nGruposLabel = new JLabel("Groups:" + _nGrupos);
		this.add(nGruposLabel);

	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {

		_time = time;
		tiempoLabel.setText("Time:" + _time);

	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {

		_time = 0;
		tiempoLabel.setText("Time:" + _time);
		_nGrupos = 0;
		nGruposLabel.setText("Groups:" + _nGrupos);
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {

	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {

		_nGrupos++;
		nGruposLabel.setText("Groups:" + _nGrupos);

	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {

	}

	@Override
	public void onDeltaTimeChanged(double dt) {

	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {

	}
}
