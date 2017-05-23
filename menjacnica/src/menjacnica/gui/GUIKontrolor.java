package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontrolor {

	public static MenjacnicaInterface sistem;

	private static MenjacnicaGUI menjacnicaProzor;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sistem = new Menjacnica();
					MenjacnicaGUI frame = new MenjacnicaGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozor = new DodajKursGUI(menjacnicaProzor);
		prozor.setLocationRelativeTo(menjacnicaProzor);
		prozor.setVisible(true);
	}

	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(menjacnicaProzor, "Da li ZAISTA zelite da izadjete iz apliacije",
				"Izlazak", JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static void prikaziAboutProzor() {
		JOptionPane.showMessageDialog(menjacnicaProzor, "Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(menjacnicaProzor);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				GUIKontrolor.sistem.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnicaProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(menjacnicaProzor);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				GUIKontrolor.sistem.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute();
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnicaProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected static void prikaziSveValute() {
		MenjacnicaTableModel model = (MenjacnicaTableModel) (MenjacnicaGUI.table.getModel());
		model.staviSveValuteUModel(GUIKontrolor.sistem.vratiKursnuListu());

	}

	public static void prikaziObrisiKursGUI() {

		if (MenjacnicaGUI.table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (MenjacnicaGUI.table.getModel());
			ObrisiKursGUI prozor = new ObrisiKursGUI(menjacnicaProzor,
					model.vratiValutu(MenjacnicaGUI.table.getSelectedRow()));
			prozor.setLocationRelativeTo(menjacnicaProzor);
			prozor.setVisible(true);
		}
	}

	public static void prikaziIzvrsiZamenuGUI() {
		if (MenjacnicaGUI.table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (MenjacnicaGUI.table.getModel());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(menjacnicaProzor,
					model.vratiValutu(MenjacnicaGUI.table.getSelectedRow()));
			prozor.setLocationRelativeTo(menjacnicaProzor);
			prozor.setVisible(true);
		}
	}
}
