package simulator.view;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ViewerWindow extends JFrame implements SimulatorObserver {

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private SimulationViewer _viewer;
	private JFrame _parent;

	public ViewerWindow(JFrame _viewerFrame, Controller _ctrl) {
		super("Simulation viewer");
		this._parent = _viewerFrame;
		this._ctrl = _ctrl;
		_viewer = new Viewer();

		initGUI();
		_ctrl.addObserver(this);
	}

	private void initGUI() {

		JPanel panel = new JPanel(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(_viewer);
		panel.add(scrollPane, BorderLayout.CENTER);
		setContentPane(panel);

		panel.add(_viewer, BorderLayout.CENTER);
		setLocationRelativeTo(_parent);

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {

				_ctrl.removeObserver(ViewerWindow.this);
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}
		});

		pack();
		if (_parent != null)
			setLocation(_parent.getLocation().x + _parent.getWidth() / 2 + getWidth() / 2,
					_parent.getLocation().y + _parent.getHeight() / 2 + getHeight() / 2);
		setVisible(true);

	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		_viewer.update();

	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_viewer.reset();

	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		_viewer.reset();

		for (BodiesGroup bg : groups.values()) {
			_viewer.addGroup(bg);
			List<Body> lista = bg.getListaRO();

			for (Body b : lista) {

				_viewer.addBody(b);
			}
		}

		_viewer.update();

	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		_viewer.addGroup(g);
		_viewer.update();
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		_viewer.addBody(b);
		_viewer.update();
	}

	@Override
	public void onDeltaTimeChanged(double dt) {

	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {

	}

}
