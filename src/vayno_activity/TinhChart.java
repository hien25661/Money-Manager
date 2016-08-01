package vayno_activity;

import android.graphics.Color;

public class TinhChart {

	public int[] TinhPhanTram(int[] cacso) {
		int tong = 0, dem = 0;
		int kq[] = new int[cacso.length];
		for (int i = 0; i < cacso.length; i++) {
			tong = tong + cacso[i];
		}
		for (int i = 0; i < cacso.length - 1; i++) {
			kq[i] = (cacso[i] * 100) / tong;
			dem = dem + kq[i];
		}
		kq[cacso.length - 1] = 100 - dem;
		return kq;
	}

	public int[] list_RandomColor(int soMau) {
		int list_color[] = new int[soMau];
		for (int i = 0; i < soMau; i++) {
			list_color[i] = RandomColor();
		}
		return list_color;
	}

	public int RandomColor() {
		return Color.rgb((int) (Math.random() * 255),
				(int) (Math.random() * 255), (int) (Math.random() * 255));
	}
}