package sochitieu;

import object.ThuChi;

public class EntryItem implements Item {

	private ThuChi tc;

	public EntryItem(ThuChi thuchi) {
		this.tc = thuchi;
	}

	public ThuChi getTc() {
		return tc;
	}

	@Override
	public boolean isSection() {
		return false;
	}

}
