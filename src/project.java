import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public class project extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;
    private flag flag;
    private Player self;
    private Player ene;
    private Qlearning obj;
    private int x,y;
    private int iteration_times;
    private ArrayList<Integer> reward;

    public project(flag f) {
		this.flag = f;
		obj= new Qlearning(game, this.flag);
		iteration_times=0;
		reward = new ArrayList<Integer>();
	}

	public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
//        System.out.println("New unit discovered " + unit.getType());
    }

    @Override
    public void onStart() {
    	reward = new ArrayList<Integer>();
    	this.iteration_times = 0;
        game = mirror.getGame();
        game.setLocalSpeed(30);
        self = game.self();
        ene =game.enemy();
        this.flag=new flag();
        obj.restart(game,flag);
        x=1;
        y=0;
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
//        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
//        int i = 0;
//        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
//        	System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
//        	for(Position position : baseLocation.getRegion().getPolygon().getPoints()){
//        		System.out.print(position + ", ");
//        	}
//        	System.out.println();
//        }
        

    }
    
    @Override
    public void onFrame(){
    	y++;
    	//text size(10)
//    	game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
//    	StringBuilder units = new StringBuilder("My units:\n");
    	if(game.enemy().getUnits().size()==1&&game.enemy().getUnits().get(0).getType()==UnitType.Terran_Command_Center){
    		System.out.println("Victory!");
        	System.out.println(reward);
        	game.restartGame();
    	}
        if(game.enemy().getUnits().size()<=1||self.getUnits().size()!=2){
        	if(flag.action_list.Move_List.size()!=0&&y<=100){
        		if(flag.index<flag.action_list.Attack_List.size()){
        			if(flag.action_list.Attack_List.get(flag.index).getHitPoints()!=flag.previous);
        			else return;
        		}
        		else if(obj.s.V.getPosition().equals
        				(flag.action_list.Move_List.get(flag.index-flag.action_list.Attack_List.size())));
        		else return;
        	}
        	y=0;
        	if(x==1){
        		System.out.println(obj.action_list.size());
    			obj.run();
    			x=0;
    			return;
        	}
    			reward.add(obj.update());
    			obj.run();
    			this.iteration_times++;
        		//System.out.println(obj.action_list.size());

        }
        else{
        	System.out.println(reward);
        	System.out.println("Reward Size:"+reward.size()+"  Iterations: "+iteration_times);
			game.restartGame();
        }

    }
    
    public static void main(String[] args) {
    	flag f = new flag();
    	project p = new project(f);
        p.run();
    }
    
}