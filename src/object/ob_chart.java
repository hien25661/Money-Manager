package object;

import java.util.List;

public class ob_chart {

	List<Integer> KQTong;
	List<Integer> KQTheloai;

	public List<Integer> getKQTong() {
		return KQTong;
	}
	public void setKQTong(List<Integer> kQTong) {
		KQTong = kQTong;
	}


	public List<Integer> getKQTheloai() {
		return KQTheloai;
	}

	public void setKQTheloai(List<Integer> kQTheloai) {
		KQTheloai = kQTheloai;
	}

	public ob_chart() {
		super();
		KQTong = null;
		KQTheloai = null;
	}



}
