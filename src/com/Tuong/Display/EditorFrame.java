package com.Tuong.Display;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.Tuong.EntityType.EntityType;
import com.Tuong.GameManager.GameManager;
import com.Tuong.GameManager.Stage;

public class EditorFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField textField;
	public JLabel lblId;
	public JCheckBox chckbxDiRaDang;
	public GameManager manager;
	public JCheckBox showCollide;
	public JCheckBox chckbxEditPath;
	private JButton btnClearPath;
	public JComboBox<?> comboBox;
	public JCheckBox chckbxHienThiEntityh;

	public void saveMap(String name) {
		try {
			File f = new File("Data/Map");
			File file = new File(f.getPath(), name + ".json");
			file.createNewFile();
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			for (int i = 0; i <= 999; ++i) {
				for (int j = 0; j <= 999; ++j) {
					writer.print(manager.map_id[i][j] + " ");
				}
				writer.println();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readTexture(String name) {
		try {
			File file = new File("Data/Map");
			File f = new File(file, name + ".json");
			try (Stream<String> lines = Files.lines(Paths.get(f.getPath()), StandardCharsets.UTF_8)) {
				int n = 0;
				for (String line : ((Iterable<String>) lines::iterator)) {
					int m = 0;
					for (String s : line.split(" ")) {
						manager.map_id[n][m] = s;
						m++;
					}
					++n;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void savePath(String name) {
		try {
			File f = new File("Map/Paths");
			File file = new File(f.getPath(), name + ".json");
			file.createNewFile();
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			for(Point point : manager.path_edit) writer.println(point.x+" "+point.y);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Element element;
	public JTextField textField_1;
	public JCheckBox chckbxHienThiLocation;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EditorFrame(GameManager manager) {
		element = new Element(manager);
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				manager.game_stage = Stage.MENU;
				element.dispose();
			}
		});
		this.manager = manager;
		setTitle("Editor Pane");
		setAlwaysOnTop(true);
		setBounds(100, 100, 417, 297);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		textField = new JTextField();
		textField.setColumns(10);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveMap(textField.getText());
			}
		});

		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readTexture(textField.getText());
			}
		});

		JButton btnNewButton = new JButton("Save Path");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePath(textField.getText());
			}
		});
		JTextArea textArea = new JTextArea();
		String content = "";
		File f = new File("Data/Map");
		for (File file : f.listFiles())
			content += file.getName() + "\n";
		textArea.setText(content);

		JButton btnNewButton_2 = new JButton("Refresh list");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String content = "";
				File f = new File("Data/Map");
				for (File file : f.listFiles())
					content += file.getName() + "\n";
				textArea.setText(content);

			}
		});

		lblId = new JLabel("ID:");

		chckbxDiRaDang = new JCheckBox("Hien thi mat tren");
		
		showCollide = new JCheckBox("Hien thi vat can");
		
		chckbxEditPath = new JCheckBox("Edit path");
		
		btnClearPath = new JButton("Clear Path");
		btnClearPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				manager.path_edit.clear();
			}
		});
		
		comboBox = new JComboBox(EntityType.values());
		
		chckbxHienThiEntityh = new JCheckBox("Hien thi entity");
		chckbxHienThiEntityh.setSelected(true);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		chckbxHienThiLocation = new JCheckBox("Hien thi location");
		chckbxHienThiLocation.setSelected(true);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(134)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNewButton_2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
								.addComponent(comboBox, 0, 237, Short.MAX_VALUE)
								.addComponent(lblId)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
									.addComponent(textField)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(btnSave)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnLoad, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnClearPath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE))
							.addGap(10)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxDiRaDang)
								.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
								.addComponent(showCollide, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxEditPath, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxHienThiEntityh, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxHienThiLocation, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxDiRaDang)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(showCollide)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxEditPath)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxHienThiEntityh))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnSave)
								.addComponent(btnLoad))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnClearPath)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(29)
							.addComponent(lblId)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_2)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxHienThiLocation))))
					.addContainerGap(15, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		setVisible(true);
	}
}