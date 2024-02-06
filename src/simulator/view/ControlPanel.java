package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ControlPanel extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _quitButton;
	private JButton _fileOpen;
	private JFileChooser _fileChooser;
	private JButton _stop;
	private JButton _play;
	private JButton _forceLaws;
	private JButton _viewerButton;

	private JSpinner _stepSpinner;
	private JTextField _deltaTimeText;
	private Controller _ctrl;

	private static final int defaultSteps = 10000;
	private int _steps;
	private JTextField dtField;

	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_steps = defaultSteps;
		_stop = new JButton();
		initGUI();
		ctrl.addObserver(this);

	}

	private void initGUI() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		add(_toolaBar, BorderLayout.PAGE_START);

		_toolaBar.addSeparator();

		// Selector de ficheros
		_fileOpen = new JButton();
		// carpeta default
		File input = new File("resources/examples/input");
		_fc = new JFileChooser();
		_fc.setCurrentDirectory(input);
		_fileOpen.setIcon(new ImageIcon("resources/icons/open.png"));

		// se enlaza el fileopen con el JfileChooser
		_fileOpen.addActionListener(e -> {

			// abre cuadro de dialogo para q el usuario elija
			_fc.showOpenDialog(Utils.getWindow(this));

			// si el archivo existe
			if (_fc.getSelectedFile() != null) {
				_ctrl.reset();

				// carag la info en controler
				FileInputStream is;
				try {
					is = new FileInputStream(_fc.getSelectedFile().getPath());
					_ctrl.loadData(is);
				} catch (FileNotFoundException e1) {
					// Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		_fileOpen.setToolTipText("Load an input file");
		_toolaBar.add(_fileOpen);

		_toolaBar.addSeparator();

		// forcelawsdialogn

		_forceLaws = new JButton();
		JFrame _physicsFrame = new JFrame();
		_forceLaws.setIcon(new ImageIcon("resources/icons/physics.png"));
		_forceLaws.setToolTipText("Select force laws for each group");
		_forceLaws.addActionListener((e) -> new ForceLawsDialog(_physicsFrame, _ctrl).open());

		_toolaBar.add(_forceLaws);
		_toolaBar.addSeparator();

		// viewerWindow

		_viewerButton = new JButton();
		JFrame _viewerFrame = new JFrame();
		_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_viewerButton.addActionListener((e) -> {

			ViewerWindow ventanaViewer = new ViewerWindow(_viewerFrame, _ctrl);
			ventanaViewer.setVisible(true);
			ventanaViewer.setLocationRelativeTo(null);
		});

		_toolaBar.add(_viewerButton);
		_toolaBar.addSeparator();

		// play
		_play = new JButton();
		_play.setIcon(new ImageIcon("resources/icons/run.png"));
		_play.setToolTipText("Run simulation");
		_play.addActionListener(e -> playAction());
		_toolaBar.add(_play);

		_toolaBar.addSeparator();

		// stop
		_stop = new JButton();
		_stop.setIcon(new ImageIcon("resources/icons/stop.png"));
		_stop.setToolTipText("Stop simulation");
		_stop.addActionListener(e -> _stopped = true);
		_toolaBar.add(_stop);

		_toolaBar.addSeparator();

		// Jspinner steps
		_toolaBar.add(new JLabel("Steps:"));

		SpinnerModel modelSteps = new SpinnerNumberModel(_steps, 0, 10000, 1);
		_stepSpinner = new JSpinner(modelSteps);

		modelSteps.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_steps = (int) _stepSpinner.getValue();
			}
		});

		_stepSpinner.setValue(_steps);
		_stepSpinner.setToolTipText("Steps : 1-10000");
		_stepSpinner.setPreferredSize(getPreferredSize());

		_toolaBar.add(_stepSpinner);
		_toolaBar.addSeparator();

		// jtextField detlta time
		_toolaBar.add(new JLabel("Delta-Time:"));
		_deltaTimeText = new JTextField(dtField.toString());
		_deltaTimeText.setToolTipText("Delta-Time");
		_toolaBar.add(_deltaTimeText);

		_toolaBar.addSeparator();
		_toolaBar.add(Box.createGlue()); // this aligns the button to the right

		// Quit Button
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> Utils.quit(this));
		_toolaBar.add(_quitButton);

	}

	private void playAction() {

		_stopped = false;

		// desactiva todos los botones q no sean el stop
		for (Component c : _toolaBar.getComponents()) {

			if (!c.equals(_stop))
				c.setEnabled(false);
		}

		// runea la simulacion con el valor del spinner
		_ctrl.setDeltaTime(Double.parseDouble(_deltaTimeText.getText()));
		run_sim((int) _stepSpinner.getValue());

	}

	private void fileOpenAction() {

		// abre la ventana para seleccionar el archivo
		int seleccion = _fc.showOpenDialog(Utils.getWindow(this));
		// el usuario pincha en aceptar
		if (seleccion == JFileChooser.APPROVE_OPTION) {

			File fichero = _fc.getSelectedFile();

			try {
				_ctrl.loadData(new FileInputStream(fichero));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			_ctrl.reset();
		}
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		dtField = new JTextField(Double.toString(_ctrl.getDeltaTime()));
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
	}

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				Utils.showErrorMsg("Error al iniciar la ejecución");
				activarBotones();
				_stopped = true;
				return;
			}

			SwingUtilities.invokeLater(() -> run_sim(n - 1));
		} else {
			// Utils.showErrorMsg("Error al iniciar la ejecución");
			activarBotones();
			_stopped = true;
		}
	}

	private void activarBotones() {

		// activa todos los botones de toolbar
		for (Component c : _toolaBar.getComponents()) {

			c.setEnabled(true);
		}
	}
}
