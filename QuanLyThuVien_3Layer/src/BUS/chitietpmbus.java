package BUS;

import java.util.ArrayList;

import DAL.PhieuMuonDAL;
import DAL.chitietpmDAL;
import DTO.ChiTieuPMDTO;
import DTO.PhieuMuon;

public class chitietpmbus {
			
	ChiTieuPMDTO pmdal = new ChiTieuPMDTO(); 
	private ArrayList<PhieuMuon> listpm = new ArrayList<PhieuMuon>();

	public ArrayList<ChiTieuPMDTO> getdanhsachpm() {

		return chitietpmDAL.getdanhsachphieumuon();
	}

	public int thempm(ChiTieuPMDTO pm) {
		return chitietpmDAL.themctpm(pm);
	}

	public int suapm(ChiTieuPMDTO pm) {
		return chitietpmDAL.suactpm(pm);
	}

	public int xoapm(ChiTieuPMDTO pm) {
		return chitietpmDAL.xoactpm(pm);
	}

	public static chitietpmbus iBus = null;

	public static chitietpmbus gI() {
		if (iBus == null) {
			iBus = new chitietpmbus();
		}

		return iBus;
	}
}
