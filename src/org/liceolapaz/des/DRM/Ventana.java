package org.liceolapaz.des.DRM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

/*
 * BASIC TEXT EDITOR. "BLACK BOARD"
 * 
 *                           ,     
                      ,   |     
   _,,._              |  0'     
 ,'     `.__,--.     0'         
/   .--.        |           ,,, 
| [=========|==|==|=|==|=|==___]
\   "--"  __    |           ''' 
 `._   _,'  `--'                
    ""'     ,   ,0     ,        
        |)  |)   ,'|        
  ____     0'   '   | 0'        
  |  |             0'           
 0' 0'
       ___ 
 * 
 * AUTHOR: Daniel Romero.
 */
public class Ventana extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textpane;
	private static String ruta = null;

	public Ventana() {
		// set location and size
		setBounds(500,200,1024,768);
		// give the window a starting title
		setTitle("New Document");
		// set an Icon for the window
		setIconImage(new ImageIcon(getClass().getResource("/img/textEdtior.png")).getImage());
		// new JPanel
		textpane = new JTextArea();
		add(textpane);
		// background color and border
		textpane.setBackground(Color.BLACK);
		textpane.setForeground(Color.WHITE);
		textpane.setFont(new Font("Helvetica", Font.PLAIN, 14));
		textpane.setCaretColor(Color.WHITE);
		textpane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10));
		JScrollPane scroll = new JScrollPane(textpane);
		add(scroll, BorderLayout.CENTER);
		
		initializeMenu();
		
	}

	private void initializeMenu() {
		JMenuBar menubar = new JMenuBar();
		JMenu mainMenu = new JMenu("Files");
		mainMenu.setFont(new Font("Helvetica", Font.PLAIN, 18));
		mainMenu.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem novo = new JMenuItem();
		novo.setText("New");
		novo.setIcon(new ImageIcon(getClass().getResource("/img/new-file.png")));
		novo.setFont(new Font("Helvetica", Font.PLAIN, 16));
		novo.setMnemonic(KeyEvent.VK_N);
		novo.setAccelerator(KeyStroke.getKeyStroke("control N"));
		novo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newFile();
			}

		});
		
		JMenuItem open = new JMenuItem();
		open.setText("Open");
		open.setIcon(new ImageIcon(getClass().getResource("/img/open-archive.png")));
		open.setFont(new Font("Helvetica", Font.PLAIN, 16));
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke("control O"));
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}

		});
		
		JMenuItem save = new JMenuItem();
		save.setText("Save");
		save.setIcon(new ImageIcon(getClass().getResource("/img/save.png")));
		save.setFont(new Font("Helvetica", Font.PLAIN, 16));
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke("control S"));
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}

		});
		
		JMenuItem saveAs = new JMenuItem();
		saveAs.setText("Save as...");
		saveAs.setIcon(new ImageIcon(getClass().getResource("/img/save.png")));
		saveAs.setFont(new Font("Helvetica", Font.PLAIN, 16));
		saveAs.setMnemonic(KeyEvent.VK_G);
		saveAs.setAccelerator(KeyStroke.getKeyStroke("control G"));
		saveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveFileAs();
			}

		});
		
		JMenuItem exit = new JMenuItem();
		exit.setText("Exit");
		exit.setIcon(new ImageIcon(getClass().getResource("/img/logout.png")));
		exit.setFont(new Font("Helvetica", Font.PLAIN, 16));
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setAccelerator(KeyStroke.getKeyStroke("control E"));
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}

		});
		
		menubar.add(mainMenu);
		mainMenu.add(novo);
		mainMenu.add(open);
		mainMenu.add(save);
		mainMenu.add(saveAs);
		mainMenu.add(exit);
		setJMenuBar(menubar);
		
	}
	
	public void newFile() {
		ruta = null;
		textpane.setText("");
		setTitle("New Document");
	}
	
	public void openFile() {
		int option = JOptionPane.showConfirmDialog(null,"Do you wish to open a new file? All unsaved data will be lost.", "Open file", JOptionPane.YES_NO_OPTION);
		switch (option) {
		case 0:
			if((ruta = JOptionPane.showInputDialog("Write the desired route: ")) != null ) {
				textpane.setText("");
				ArrayList<String> contenidoRuta = leerFicheroTexto(ruta);			
				mostrarContenido(contenidoRuta);
			}
			setTitle(ruta);
			
		case 1:
			if(ruta == null) {
				setTitle("New Document");				
			}else {
				setTitle(ruta);
			}
			break;
		}	
		
		
	}
	
	public void saveFile() {
		if(!(ruta == null)) {
		try
		{
			PrintWriter out = new PrintWriter(ruta);
			String texto = textpane.getText();
			texto.replaceAll("\n", "\r\n");
			out.println(texto);
			out.close();
		}
		catch ( IOException e)
		{
		 JOptionPane.showMessageDialog(null, "An error ocurred, file unsaved","Error",JOptionPane.WARNING_MESSAGE);
		}
		finally {
			setTitle(ruta);
		}
		}else {
			saveFileAs();
		}
		
	}
	
	private ArrayList<String> leerFicheroTexto(String ruta) {
		ArrayList<String> contenido = new ArrayList<>();
		File f = new File(ruta);
		Scanner s;
		try {
			s = new Scanner(f);
			String linea;
			while (s.hasNextLine()) {
				linea = s.nextLine();
				linea +="\r\n";
				contenido.add(linea);
			}
			s.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,"The chosen route " + ruta + " is not a readable file.", "Error", JOptionPane.WARNING_MESSAGE);
		}
		return contenido;
	}
	
	private void mostrarContenido(ArrayList<String> contenidoRuta) {
		for (String addLine : contenidoRuta) {
			textpane.append(addLine);
		}
			}
	
	public void saveFileAs() {
		// pasar una nueva ruta donde guarde el documento, utilizar mismo sistema que saveFile
		 
		 	JFileChooser jfc = new JFileChooser();
		 	int seleccion = jfc.showOpenDialog(textpane);
		 	if(seleccion == JFileChooser.APPROVE_OPTION) {
		 		File fichero = jfc.getSelectedFile();		 				 	
			try
			{
			PrintWriter out = new PrintWriter(fichero);
			String texto = textpane.getText();
			texto.replaceAll("\n", "\r\n");
			// IMPLEMENTAR TAMBIEN EN GUARDAR
			out.println(texto);
			out.close();
			}
			catch ( IOException e)
			{
			 JOptionPane.showMessageDialog(null, "An error ocurred, file unsaved","Error",JOptionPane.WARNING_MESSAGE);
			}
			finally {
				setTitle(fichero.getAbsolutePath());
			}
		 	}
		 
		
	}
	
	public void exit() {
		System.exit(0);		
	}
}
