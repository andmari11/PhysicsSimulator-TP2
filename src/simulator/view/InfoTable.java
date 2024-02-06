package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class InfoTable extends JPanel {
	String _title;
	TableModel _tableModel;

	InfoTable(String title, TableModel tableModel) {
		_title = title;
		_tableModel = tableModel;
		initGUI();
	}

	private void initGUI() {
		// cambiar el layout del panel a BorderLayout()
		setLayout(new BorderLayout());

		// añadir un borde con título al JPanel, con el texto _title
		setBorder(BorderFactory.createTitledBorder(_title));

		// separador
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);

		// añadir un JTable (con barra de desplazamiento vertical) que use
		JScrollPane scrollPane = new JScrollPane(new JTable(_tableModel));
		add(scrollPane, BorderLayout.CENTER);

	}
}
