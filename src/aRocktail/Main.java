package aRocktail;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;

@ScriptManifest(author = "Atex", category = Category.FISHING, description = "Fishes rocktails in living rock caverns.", name = "aRocktail", servers = { "Ikov" }, version = 0.1)

public class Main extends Script implements Paintable {
	public static long startTime = System.currentTimeMillis();
	private final ArrayList<Strategy> strategies = new ArrayList<Strategy>();
	public static String status = "Idle";
	public static int startingCount = Inventory.getCount(true, Action.idLivingMinerals);
	public static int count = 0;
	
	@Override
	public boolean onExecute() {
		startTime = System.currentTimeMillis();
		strategies.add(new Action());
		provide(strategies);
		return true;
	}
	@Override
	public void onFinish() {
		
	}
	@Override
	public void paint(Graphics iFace) {
		if(Inventory.getCount(true, Action.idLivingMinerals) > 0) {
			count = (startingCount - Inventory.getCount(true, Action.idLivingMinerals));
		}
		
		iFace.setColor(Color.white);
    	iFace.setFont(new Font("Verdana", Font.BOLD, 12));
    	iFace.drawString("aRocktail", 545, 350);
    	iFace.drawString(runTime(startTime), 545, 363);
    	iFace.drawString("Status: "+status, 545, 376);
    	iFace.drawString("Count: "+count, 545, 389);
	}
	public int amountPerHour() {
		long currentTime = System.currentTimeMillis() - startTime;
		int rate = 0;
		rate = (startingCount - Inventory.getCount(true, Action.idLivingMinerals)) / ((int)(currentTime) / (3600000));
		return rate;
	}
	public static String runTime(long start) {
		DecimalFormat df = new DecimalFormat("00");
		long currentTime = System.currentTimeMillis() - start;
		long hours = currentTime / (3600000);
		currentTime -= hours * (3600000);
		long minutes = currentTime / (60000);
		currentTime -= minutes * (60000);
		long seconds = currentTime / (1000);
		return df.format(hours) + ":" + df.format(minutes) + ":" + df.format(seconds);
	}
}

