package com.windanesz.wizardryfates.integration;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import com.windanesz.wizardryfates.Settings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class FatesReskillableIntegration {

	public static final String RESKILLABLE_MOD_ID = "reskillable";

	private static ResourceLocation MAGIC = new ResourceLocation(RESKILLABLE_MOD_ID, "magic");

	private static boolean reskillableLoaded;

	public static void init() {

		reskillableLoaded = Loader.isModLoaded(RESKILLABLE_MOD_ID);
		// nothing specific to do here for now
	}

	public static boolean enabled() {
		return Settings.settings.reskillable_integration && reskillableLoaded;
	}

	public static float getProgressPercent(EntityPlayer player) {
		Skill magicSkill = ReskillableRegistries.SKILLS.getValue(MAGIC);
		if (magicSkill != null) {
			PlayerSkillInfo playerSkillInfo = PlayerDataHandler.get(player).getSkillInfo(magicSkill);
			float playerlevel = playerSkillInfo.getLevel();
			float maxLevel = magicSkill.getCap();
			float percent = playerlevel / maxLevel;
			return percent;
		}

		return 1.0f;
	}

}
