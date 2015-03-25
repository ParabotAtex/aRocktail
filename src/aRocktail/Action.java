package aRocktail;

import org.parabot.core.Context;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.wrappers.Area;
import org.rev317.min.api.wrappers.Tile;
import org.rev317.min.api.wrappers.TilePath;
import org.rev317.min.api.wrappers.Npc;

public class Action implements Strategy {
	int idRod =308;
	public static int idLivingMinerals=15264;
	int idFishingSpot = 8842;
	public static int startingCount = Inventory.getCount(true, idLivingMinerals);
	Tile[] pathToFish = {
			new Tile (3652, 5127),
			new Tile (3643, 5136),
			new Tile (3634, 5139),
	};
	Tile[] pathToBank = {
			new Tile (3643, 5136),
			new Tile (3652, 5127),
			new Tile (3653, 5115),
	};
	Area bank = new Area(new Tile(3651,5116), new Tile(3655, 5116), new Tile(3655,5112), new Tile(3651, 5112));
	Area fishing = new Area(new Tile(3632,5140), new Tile(3635,5140), new Tile(3635,5137), new Tile(3632,5137));
	@Override
	public boolean activate() {
		if(Players.getMyPlayer().getAnimation() == 622) {
			return false;
		} else {
			return true;
		}
	}
	@Override
	public void execute() {
		if(Inventory.getCount(true, idLivingMinerals) < 1) {
			 System.out.println("Out of living minerals, stopping.");
			 System.out.println("Last numbers");
			 System.out.println("Time run: "+(Main.runTime(Main.startTime)));
			 System.out.println("Count: "+Main.count);
			 Context.getInstance().getRunningScript().setState(Script.STATE_STOPPED);
		}
		Npc fishingSpot = Npcs.getClosest(idFishingSpot);
		if(fishingSpot != null && !Inventory.isFull()) {
			Main.status = "Fishing";
			fishingSpot.interact(0);
			Time.sleep(1000);
		}
		if(Inventory.isFull() && fishing.contains(Players.getMyPlayer().getLocation())) {	
			Main.status = "Banking";
			TilePath pathBank = new TilePath(pathToBank);
			while(!bank.contains(Players.getMyPlayer().getLocation())) {
				if(!pathBank.hasReached()) {
				      pathBank.traverse();
				      Time.sleep(new SleepCondition() {
				                @Override
				                public boolean isValid() {
				                    return pathBank.hasReached();
				                }
				            }, 1000);
				}
			}
		}
		if(bank.contains(Players.getMyPlayer().getLocation()) && Inventory.isFull()) {
			Menu.sendAction(502, 1275450546, 50, 41);
			Time.sleep(new SleepCondition() {
				@Override
				public boolean isValid() {
					return Game.getOpenInterfaceId() == 4465;
				}
			},1000);
			if(Game.getOpenInterfaceId() == 4465) {
				Time.sleep(500);
				Menu.sendAction(431, 15270, 4, 7423, 3);
				Time.sleep(1000);
			}
		}
		if(bank.contains(Players.getMyPlayer().getLocation()) && !Inventory.isFull()) {
			Main.status = "Walking to spot";
			TilePath pathSpot = new TilePath(pathToFish);
			while(!fishing.contains(Players.getMyPlayer().getLocation())) {
				if(!pathSpot.hasReached()) {
				      pathSpot.traverse();
				      Time.sleep(new SleepCondition() {
				                @Override
				                public boolean isValid() {
				                    return pathSpot.hasReached();
				                }
				            }, 1000);

				}
			}
		}
	}
}