package com.windanesz.wizardryfates;

import com.windanesz.wizardryfates.command.CommandSetDiscipline;
import com.windanesz.wizardryfates.handler.Discipline;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = WizardryFates.MODID, name = WizardryFates.NAME, version = WizardryFates.VERSION, acceptedMinecraftVersions = WizardryFates.MC_VERSION, dependencies = "required-after:ebwizardry@[4.2,4.4)")
public class WizardryFates {

	public static final String MODID = "wizardryfates";
	public static final String NAME = "Wizardry Fates by Dan";
	public static final String VERSION = "1.0.1";
	public static final String MC_VERSION = "[1.12.2]";

	public static final Random rand = new Random();

	/**
	 * Static instance of the {@link Settings} object for WizardryFates.
	 */
	public static Settings settings = new Settings();

	public static Logger logger;

	// The instance of wizardry that Forge uses.
	@Mod.Instance(WizardryFates.MODID)
	public static WizardryFates instance;

	// Location of the proxy code, used by Forge.
	@SidedProxy(clientSide = "com.windanesz.wizardryfates.client.ClientProxy", serverSide = "com.windanesz.wizardryfates.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		settings = new Settings();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		// register WizardData attributes
		Discipline.init();

		MinecraftForge.EVENT_BUS.register(instance); // Since there's already an instance we might as well use it
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new CommandSetDiscipline());

	}

}
