package com.Tuong.Display;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.Tuong.GameManager.GameManager;
import com.Tuong.GameManager.Stage;

public class Element extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public GameManager manager;
	
	public Element(GameManager manager) {
		this.manager = manager;
		setTitle("Select texture");
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				manager.editor_pane.dispose();
				manager.game_stage = Stage.MENU;
			}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				manager.img_review = (int)(e.getX()/96 + (e.getY()/96)*Math.round(Math.sqrt(manager.display.environment.size())));
				manager.interact.type_img = manager.img_review;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		setAlwaysOnTop(true);
		setBounds(100, 100, 522, 498);
		contentPane = new DrawPane();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGap(0, 424, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGap(0, 251, Short.MAX_VALUE)
		);
		contentPane.setLayout(gl_contentPane);
		setVisible(true);
	}
	class DrawPane extends JPanel{
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g){
    		int k = 0;
    		for(int i = 0; i < Math.round(Math.sqrt(manager.display.environment.size())); i ++)for(int j = 0; j < Math.round(Math.sqrt(manager.display.environment.size())); j ++){
    			if(k == manager.display.environment.size()) break;
    			g.drawImage(manager.display.environment.get(k).texture,j*96,i*96,this);
    			g.drawRect(j*96, i*96, 96, 96);
    			k++;
    		}
         }
    }
}
