import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;
 

public class Qlearning{
 
    // path finding
	private flag flag;
    final double alpha = 0.1;
    final double gamma = 0.9;
    public state s;
    public int position_index;
    public Game game;
    public HashMap<state,actions> action_list ;
    actions Actions;
    int selfHit;
    int eneHit;
    int reward;
    int index;
 
    public Qlearning(Game game,flag flag) {
    	this.game = game;
    	this.flag = flag;
    	action_list= new HashMap<state,actions>();
    }
 
    public void restart(Game game,flag flag){
    	this.game =game;
    	this.flag=flag;
    }
 
 
    public void run() {
        /*
         1. Set parameter , and environment reward matrix R 
         2. Initialize matrix Q as zero matrix 
         3. For each episode: Select random initial state 
            Do while not reach goal state o 
                Select one among all possible actions for the current state o 
                Using this possible action, consider to go to the next state o 
                Get maximum Q value of this next state based on all possible actions o 
                Compute o Set the next state as the current state
         */
 
        // For each episode

//    	synchronized(flag){
//    		if(flag.index<flag.action_list.Attack_List.size()){
//            	while(flag.a!=0&&flag.action_list.Attack_List.get(flag.index).getHitPoints()!=flag.previous){
//        			try{
//        				flag.wait();			
//        			}
//        				catch(InterruptedException e){
//        					System.out.println("error!");
//        				}
//                	}
//    		}
//    		else{
//            	while(flag.a!=0&&s.V.getPosition().equals(flag.action_list.Move_List.get(flag.index-flag.action_list.Attack_List.size()+1))){
//        			try{
//        				flag.wait();			
//        			}
//        				catch(InterruptedException e){
//        					System.out.println("error!");
//        				}
//                	}
//    		}

        Random rand = new Random();// train episodes
            // Select random initial state
    	s = new state(game);
    	
    	if(action_list.containsKey(s))
    		Actions = this.action_list.get(s);
    	else
    		Actions = s.getAction();
    	
            	
                // Selection strategy is random in this example
                index = rand.nextInt(Actions.size());
                if(index<Actions.Attack_List.size()){
                	Unit eneUnit = Actions.Attack_List.get(index);
                    selfHit = s.selfHitPoints;
                    eneHit = s.eneHitPoints;
                    flag.previous = eneUnit.getHitPoints();
                	s.V.attack(eneUnit);               	
                }
                else{
                Position p = Actions.Move_List.get(index-Actions.Attack_List.size());
                selfHit = s.selfHitPoints;
                eneHit = s.eneHitPoints;
                s.V.move(p);
                }
                flag.action_list=Actions;
                flag.index=index;
//    	}

    }

    public int update(){
        state s_next = new state(game);
        reward  = -(s_next.eneHitPoints - eneHit - (selfHit - s_next.selfHitPoints));
    	double previous_r = Actions.q.get(index);
        double maxQ = maxReward(s_next);
        Actions.q.set(index,reward+alpha * (previous_r + gamma * maxQ - reward));
        action_list.put(s, Actions);
        return reward;
    }
    
    double maxReward(state s) {
    	actions actionsFromState;
    	if(action_list.containsKey(s)){
        actionsFromState = action_list.get(s);
    	}
    	else{
    		return 0;
    	}
        double maxValue = Double.MIN_VALUE;
        for (int i = 0; i < actionsFromState.size(); i++) {
            double value = actionsFromState.q.get(i);
 
            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }
    
//    public static void main(String[] args) {
//        long BEGIN = System.currentTimeMillis();
//        TestBot1 Bot = new TestBot1();
//        Qlearning obj = new Qlearning(Bot.game);
// 
//        obj.run();
//        obj.printResult();
//        obj.showPolicy();
// 
//        long END = System.currentTimeMillis();
//        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
//    }
}