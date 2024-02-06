package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	private Controller _ctrl;

	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	// new InfoTable("Groups", new GroupsTableModel(_ctrl));
	// new InfoTable("Bodies", new BodiesTableModel(_ctrl));

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		// crear ControlPanel y añadirlo en PAGE_START de mainPanel
		ControlPanel controlp = new ControlPanel(_ctrl);
		mainPanel.add(controlp, BorderLayout.PAGE_START);
		// crear StatusBar y añadirlo en PAGE_END de mainPanel
		StatusBar statusBar = new StatusBar(_ctrl);
		mainPanel.add(statusBar, BorderLayout.PAGE_END);
		// Definición del panel de tablas (usa un BoxLayout vertical)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		// crear la tabla de grupos y añadirla a contentPanel.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
		GroupsTableModel groupsTable = new GroupsTableModel(_ctrl);
		contentPanel.add(new InfoTable("Groups", groupsTable)).setPreferredSize(new Dimension(500, 250));

		// crear la tabla de cuerpos y añadirla a contentPanel.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
		BodiesTableModel bodiesTable = new BodiesTableModel(_ctrl);
		contentPanel.add(new InfoTable("Bodies", bodiesTable)).setPreferredSize(new Dimension(500, 250));
		// addWindowListener();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
