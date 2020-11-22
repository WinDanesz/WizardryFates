package com.windanesz.wizardryfates.handler;

import com.windanesz.wizardryfates.Settings;

public enum DisciplineMode {

	SINGLE_DISCIPLINE_MODE(1),
	MULTI_DISCIPLINE_MODE(2),
	SUB_DISCIPLINE_MODE(3);

	private int mode;

	DisciplineMode(int mode) {
		this.mode = mode;
	}

	public static DisciplineMode getActiveMode() {
		int mode = Settings.settings.global_discipline_mode;
		switch (mode) {
			case 1:
				return SINGLE_DISCIPLINE_MODE;
			case 2:
				return MULTI_DISCIPLINE_MODE;
			case 3:
				return SUB_DISCIPLINE_MODE;
			default:
				return SINGLE_DISCIPLINE_MODE;
		}
	}
}
