package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontrolor {

	public static MenjacnicaInterface sistem;

	private static MenjacnicaGUI menjacnicaProzor;

	private static DodajKursGUI dodajKursProzor;
	private static ObrisiKursGUI obrisiKursProzor;
	private static IzvrsiZamenuGUI izvrsiZamenuProzor;

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
		dodajKursProzor = new DodajKursGUI();
		dodajKursProzor.setLocationRelativeTo(menjacnicaProzor);
		dodajKursProzor.setVisible(true);
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
				sistem.ucitajIzFajla(file.getAbsolutePath());
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
			obrisiKursProzor = new ObrisiKursGUI(model.vratiValutu(MenjacnicaGUI.table.getSelectedRow()));
			obrisiKursProzor.setLocationRelativeTo(menjacnicaProzor);
			obrisiKursProzor.setVisible(true);
		}
	}

	public static void prikaziIzvrsiZamenuGUI() {
		if (MenjacnicaGUI.table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (MenjacnicaGUI.table.getModel());
			izvrsiZamenuProzor = new IzvrsiZamenuGUI(model.vratiValutu(MenjacnicaGUI.table.getSelectedRow()));
			izvrsiZamenuProzor.setLocationRelativeTo(menjacnicaProzor);
			izvrsiZamenuProzor.setVisible(true);
		}
	}

	public static void unesiKurs(int sifra, String naziv, String skraceniNaziv, String prodajni, String kupovni,
			String srednji) {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skraceniNaziv);
			valuta.setSifra(sifra);
			valuta.setProdajni(Double.parseDouble(prodajni));
			valuta.setKupovni(Double.parseDouble(kupovni));
			valuta.setSrednji(Double.parseDouble(srednji));

			// Dodavanje valute u kursnu listu
			sistem.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			prikaziSveValute();
			dodajKursProzor.dispose();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnicaProzor, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}

	}

	public static double izvrsiZamenuValute(Valuta valuta, double iznos, boolean selected) {
		return sistem.izvrsiTransakciju(valuta, selected, iznos);
	}

	public static void obrisiValutu(Valuta valuta) {
		sistem.obrisiValutu(valuta);
		prikaziSveValute();

	}

	public static boolean obrisi(Valuta valuta) {
		try {
			GUIKontrolor.obrisiValutu(valuta);
			return true;
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public static String zamena(Valuta valuta, String teks, boolean Prodaja) {
		try {
			double konacniIznos = GUIKontrolor.izvrsiZamenuValute(valuta, Double.parseDouble(teks), Prodaja);
			return ""+konacniIznos;
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
			return "";
		}
	}
}
