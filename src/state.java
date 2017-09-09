import java.util.ArrayList;

import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class state {              //action 0 = Fight;   //action 1 = retreat
public int eneCount;
public int eneHitPoints;
public int selfHitPoints;
public ArrayList<Integer> distance;
public int cooldown;
public Game game;
public Unit V;
public int minidistance;
public int minihealth;

public state(Game game){
	this.game = game;
	Player ene = game.enemy();
	Player mine = game.self();
	eneCount = ene.getUnits().size();
	eneHitPoints = 0;
	selfHitPoints = 0;
	V = null;
	for(Unit myUnit : mine.getUnits()){
		if(myUnit.getType()==UnitType.Terran_Command_Center)
			continue;
		selfHitPoints += myUnit.getHitPoints();	
		V = myUnit;
	}
	cooldown = V.getGroundWeaponCooldown();
	distance = new ArrayList<Integer>();
	minidistance = Integer.MAX_VALUE;
	for(Unit eneUnit : ene.getUnits()){
		if(eneUnit.getType()==UnitType.Terran_Command_Center)
			continue;
		eneHitPoints += eneUnit.getHitPoints();
		int x1,x2,y1,y2;
		x1 = eneUnit.getPoint().getX();
		x2 = V.getPoint().getX();
		y1 = eneUnit.getPoint().getY();
		y2 = V.getPoint().getY();
		int temp = (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
		distance.add(temp);
		if(temp<minidistance)
			minidistance = temp;
	}
	}
private double abs(double d){
	if(d<0)
		return -d;
	else 
		return d;
}
public boolean equals(Object obj){
    if (obj == null) {
        return false;
    }
    if (!state.class.isAssignableFrom(obj.getClass())) {
        return false;
    }
    final state other = (state) obj;
	if(this.cooldown==other.cooldown&&this.eneCount==other.eneCount)
		if(abs(this.minihealth-other.minihealth)/this.minihealth<=0.25)
			if(abs(this.minidistance-other.minidistance)/this.minidistance<=0.25){
				System.out.println("Hit!");
				return true;
			}
	System.out.println("false");
	return false;
}

public actions getAction(){
	
	Player ene = game.enemy();
	actions Actions = new actions();
	
	if(cooldown==0){
	Unit attack = null;
	int hel = 100;
	for(Unit eneUnit : ene.getUnits()){
		if(V.isInWeaponRange(eneUnit)){
			attack = eneUnit;
			Actions.add(attack);
		}
	}
	if(Actions.Attack_List.size()==0){
		for(Unit eneUnit : ene.getUnits())
			Actions.add(eneUnit);
		return Actions;
	}
	}
	
//	for(Unit eneUnit : ene.getUnits()){
//		if(eneUnit.getType()==UnitType.Terran_Command_Center)
//			continue;
//		int x1,x2,y1,y2;
//		x1 = eneUnit.getPoint().getX();
//		x2 = V.getPoint().getX();
//		y1 = eneUnit.getPoint().getY();
//		y2 = V.getPoint().getY();
//		eneUnit.
//		distance.add((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
//	}
	for(int i=-100;i<=25;i+=100)
		for(int j=-100;j<=25;j+=100){
			Actions.add(new Position(V.getPosition().getX()+i,V.getPosition().getY()+j));
		}
	return Actions;
	}
}
