import java.util.ArrayList;

import bwapi.Position;
import bwapi.Unit;

public class actions {
public ArrayList<Unit> Attack_List;
public ArrayList<Position> Move_List;
public ArrayList<Double> q;
public int size;

public actions(){
	Attack_List = new ArrayList<Unit>();
	Move_List = new ArrayList<Position>();
	size = 0;
	q=new ArrayList<Double>();
}

public void add(Unit target){
	this.Attack_List.add(target);
	q.add(0.0);
	size++;
}

public void add(Position move){
	this.Move_List.add(move);
	q.add(0.0);
	size++;
}
public int size(){
	return size;
}
}
