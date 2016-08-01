package object;

import util.Variable;

public class ob_list_theloai_chart {

	int list_color[];
	int list_phantram[];
	String list_theloai[];
	Variable Var = new Variable();

	public int[] getList_color() {
		return list_color;
	}

	public void setList_color(int[] list_color) {
		this.list_color = list_color;
	}

	public int[] getList_phantram() {
		return list_phantram;
	}

	public void setList_phantram(int[] list_phantram) {
		this.list_phantram = list_phantram;
	}

	public String[] getList_theloai() {
		return list_theloai;
	}

	public void setList_theloai(int[] list_theloai) {
		String dem[] = new String[list_theloai.length];
		for (int i = 0; i < list_theloai.length; i++) {
			dem[i] = Var.Expense_THELOAI[list_theloai[i]];
		}
		this.list_theloai = dem;
	}
}