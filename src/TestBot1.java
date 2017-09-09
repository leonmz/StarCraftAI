import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public class TestBot1 extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;

    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("New unit discovered " + unit.getType());
    }

    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        int i = 0;
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
        	System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
        	for(Position position : baseLocation.getRegion().getPolygon().getPoints()){
        		System.out.print(position + ", ");
        	}
        	System.out.println();
        }

    }

    @Override
    public void onFrame() {
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());

        StringBuilder units = new StringBuilder("My units:\n");

        //iterate through my units
        int isbuilding=0;
        int x=50;
        int y=50;
        for (Unit myUnit : self.getUnits()) {
        	int future_supply=self.supplyTotal()+isbuilding*6;
            units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");

            //if there's enough minerals, train an SCV
            if (myUnit.getType() == UnitType.Terran_Command_Center && self.minerals() >= 50 && self.supplyUsed()+4<future_supply) {
            	x=myUnit.getInitialTilePosition().getX();
            	y=myUnit.getInitialTilePosition().getY();
                myUnit.train(UnitType.Terran_SCV);
            }

            //if it's a worker and it's idle, send it to the closest mineral patch
            if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                Unit closestMineral = null;

                //find the closest mineral
                for (Unit neutralUnit : game.neutral().getUnits()) {
                    if (neutralUnit.getType().isMineralField()) {
                        if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
                            closestMineral = neutralUnit;
                        }
                    }
                }

                //if a mineral patch was found, send the worker to gather it
                if (closestMineral != null) {
                    myUnit.gather(closestMineral, false);
                }
            }
            if (myUnit.getType().isWorker() && self.minerals()>=100 && self.supplyUsed()+4>future_supply) {
            	//for(;!myUnit.canBuild(UnitType.Terran_Supply_Depot, new TilePosition(x, y));x--);
            	int i=0,j=0;
            	for(i=x;i<96;){
            		for(j=y;j<96;){
            			if(myUnit.canBuild(UnitType.Terran_Supply_Depot, new TilePosition(i, j)))
            				break;
            			if(x>70)
            				i++;
            			else
            				i--;
            			if(y>70)
            				j++;
            			else
            				j--;
            		}
            	}
            	myUnit.build(UnitType.Terran_Supply_Depot, new TilePosition(i,j));
            	isbuilding++;
            }
        }

        //draw my units on screen
        game.drawTextScreen(10, 25, units.toString());
    }

    public static void main(String[] args) {
        new TestBot1().run();
    }
}