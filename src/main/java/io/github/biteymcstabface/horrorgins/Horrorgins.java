package io.github.biteymcstabface.horrorgins;

import io.github.biteymcstabface.horrorgins.factories.PowerFactories;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Horrorgins implements ModInitializer {

    @Override
    public void onInitialize() { //when the program is run
        PowerFactories.register(); //registers the PowerFactories class
        Horrorgins.LOGGER.info("Horrorgins has initialised. Spooky.");
        Horrorgins.LOGGER.info("GNU Sir Terry Pratchett");
    }
    public static final String MODID = "horrorgins"; //creates a string variable called MODID and sets it to the name of the mod
    public static final Logger LOGGER = LogManager.getLogger(MODID); //creates a logger, for use in

    public static Identifier identifier(String path){ //A method that can be easily called when wanting to create an identifier
        return new Identifier(MODID, path);
    }
}
