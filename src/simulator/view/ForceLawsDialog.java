package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ForceLawsDialog extends JDialog implements SimulatorObserver {

	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<String> _lawsModel;
	private DefaultComboBoxModel<String> _groupsModel;
	private JComboBox<String> _comboLaws;
	private JComboBox<String> _comboGroups;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _forceLawsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	private int _status;
	private Map<String, BodiesGroup> _groupsMap;
	private JButton _cancel;
	private JButton _ok;

	ForceLawsDialog(Frame parent, Controller ctrl) {

		super(parent, true);
		this._ctrl = ctrl;
		this._status = 0;
		this._forceLawsInfo = ctrl.getForceLawsInfo();
		initGUI();

		ctrl.addObserver(this);

	}

	private void initGUI() {

		setTitle("Force Laws Selection");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(
				"Selecciona una ley de fuerza y los parametros correspondientes (si están vacios se usaran los predeterminados"));
		panel.add(new JLabel("Parametros sin valor"));
		panel.setAlignmentX(LEFT_ALIGNMENT);

		mainPanel.add(panel);

		// _forceLawsInfo se usará para establecer la información en la tabla
		_forceLawsInfo = _ctrl.getForceLawsInfo();
		// crear un JTable que use _dataTableModel, y añadirla al panel
		_dataTableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// hacer editable solo la columna 1
				return column == 1;
			}
		};

		// table
		_dataTableModel.setColumnIdentifiers(_headers);

		JTable dataTable = new JTable(_dataTableModel);

		dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainPanel.add(dataTable);

		JScrollPane dataTableScroll = new JScrollPane(dataTable);
		mainPanel.add(dataTableScroll);

		_lawsModel = new DefaultComboBoxModel<>();
		_groupsModel = new DefaultComboBoxModel<>();
		_comboGroups = new JComboBox<>(_groupsModel);
		_groupsMap = new HashMap<String, BodiesGroup>(_ctrl.getGroupsList());

		for (String gid : _groupsMap.keySet()) {

			_groupsModel.addElement(gid);
		}

		// añadir la descripción de todas las leyes de fuerza a _lawsModel

		for (JSONObject jo : _forceLawsInfo) {

			_lawsModel.addElement(jo.getString("desc"));
		}

		// crear un combobox que use _lawsModel y añadirlo al panel

		_comboLaws = new JComboBox<>(_lawsModel);
		_comboLaws.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				updateDataTable();
			}

		});
		
		//muestra algo en la tabla ya
		updateDataTable();


		JPanel selectedPanel = new JPanel();

		JLabel flLabel = new JLabel("Force Law: ");
		flLabel.setAlignmentX(CENTER_ALIGNMENT);
		_comboLaws.setAlignmentX(CENTER_ALIGNMENT);

		selectedPanel.add(flLabel);
		selectedPanel.add(_comboLaws);

		// crear un combobox que use _groupsModel y añadirlo al panel

		_comboGroups.setAlignmentX(CENTER_ALIGNMENT);

		JLabel groupLabel = new JLabel("Groups: ");
		groupLabel.setAlignmentX(CENTER_ALIGNMENT);

		selectedPanel.add(groupLabel);
		selectedPanel.add(_comboGroups);

		mainPanel.add(selectedPanel);

		// crear los botones OK y Cancel y añadirlos al panel

		_ok = new JButton("OK");
		_ok.addActionListener((e) -> {

			JSONObject jData = new JSONObject();
			try {
				for (int j = 0; j < _dataTableModel.getRowCount(); j++) {

//				es cVector de mtfp??
					if (_dataTableModel.getValueAt(j, 0) == "c") {

						// obviamos el primer caracter
						String in = (String) _dataTableModel.getValueAt(j, 1);

						if (in == null) {
							
							throw new Exception("Faltan datos");
						}

						// pillamos los valores x e y
						String[] infoTotal = in.substring(1, in.length() - 1).split(",");

//					System.out.println(in);
//					System.out.println(infoTotal.toString());

						// vector posicion
						Vector2D c = new Vector2D(Double.parseDouble(infoTotal[0]), Double.parseDouble(infoTotal[1]));
						JSONArray jArrayC = new JSONArray();
						jArrayC.put(c.getX());
						jArrayC.put(c.getY());

						jData.put((String) _dataTableModel.getValueAt(j, 0), jArrayC);
					} else
						jData.put((String) _dataTableModel.getValueAt(j, 0), _dataTableModel.getValueAt(j, 1));
				}

				JSONObject info = new JSONObject();
				info.put("type", _forceLawsInfo.get(_comboLaws.getSelectedIndex()).getString("type"));
				info.put("data", jData);
				_ctrl.setForcesLaws((String) _groupsModel.getSelectedItem(), info);
				
			} catch (Exception ex) {
				//evita errores al darle ok con force laws vacio
			}

			_status = 1;
			setVisible(false);
			
		});

		// cancel button
		_cancel = new JButton("CANCEL");
		_cancel.addActionListener((e) -> {

			_status = 0;
			setVisible(false);
		});

		JPanel buttonsP = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsP.add(_ok);
		buttonsP.add(_cancel);
		mainPanel.add(buttonsP);

		setPreferredSize(new Dimension(700, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	void updateDataTable() {
		
		// guardamos el numero de rows pq sino se va reduciendo ene l bucle for
		int rowsAnteriores = _dataTableModel.getRowCount();

		// vaciamos la tabla
		for (int i = 0; i < rowsAnteriores; i++) {

			_dataTableModel.removeRow(0);
		}

		String lawSelected = (String) _comboLaws.getSelectedItem();
		int i = 0;

		// buscamos el indice de la ley elegida
		while ((_ctrl.getForceLawsInfo().get(i).get("desc") != lawSelected)) {

			i++;
		}

		JSONObject data = _ctrl.getForceLawsInfo().get(i).getJSONObject("data");

		// dependiendo de la
		switch (i) {
		// mtfp
		case 0:

			Object[] g = { "g", null, data.get("g") };
			Object[] c = { "c", null, data.get("c") };

			_dataTableModel.insertRow(0, g);
			_dataTableModel.insertRow(0, c);

			_dataTableModel.fireTableStructureChanged();
			break;
		// newton gravity
		case 1:
			Object[] G = { "G", null, data.get("G") };

			_dataTableModel.insertRow(0, G);
			_dataTableModel.fireTableStructureChanged();
			break;
		// no force
		case 2:
			_dataTableModel.fireTableStructureChanged();
			break;

		}
	}
	
	public int open() {
		if (_groupsModel.getSize() == 0)
			return _status;
		// Establecer la posición de la ventana de diálogo de tal manera que se
		// abra en el centro de la ventana principal
		this.setLocationRelativeTo(this.getParent());
		pack();
		setVisible(true);
		return _status;
	}

	private void updategroupsList() {

		_groupsMap.clear();
		for (String gid : _groupsMap.keySet()) {

			_groupsModel.addElement(gid);
		}
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		updategroupsList();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		updategroupsList();

	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// updategroupsList();

		_groupsMap.put(g.getId(), g);

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