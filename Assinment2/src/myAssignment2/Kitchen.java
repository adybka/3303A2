/** Assignment 2
 * @author Andrew Dybka
 * @studentID 101041087
 * 
 * READ ME describes the objective and how the program works.
 */
package myAssignment2;

import java.util.ArrayList;
import java.util.Random;

public class Kitchen {
	
	private static String BREAD="bread", PB = "peanut butter", JAM = "jam";//all ingredients
	private ArrayList<String> table, ingriedents;
	private static int count;
	private Random rand;
	private boolean empty;//boolean for if table is empty
	
	
	
	public Kitchen(){
		//create random object
		rand = new Random(); 
		//list for whats on table and total ingredients
		table = new ArrayList<String>();
		ingriedents = new ArrayList<String>();
		//add all ingredients
		ingriedents.add(BREAD);
		ingriedents.add(PB);
		ingriedents.add(JAM);
		//tables starts as emtpy
		empty = true;
		
	}
	
	//put class called by agent
	public void put()throws InterruptedException {
		while(count<20) {
			synchronized(this) {//synch this instance
				//wait if kitchen is not empty
				while(!empty) {
					wait();
				}
				//clear table, get two ingredients from ingredient list add to table
				table.clear();
				for (int i = 0; i < 2; i++) {
					int randomIndex = rand.nextInt(ingriedents.size());
					String randomElement = ingriedents.get(randomIndex);
					ingriedents .remove(randomElement);
					table.add(randomElement);
				}
				//refill ingredient list
				ingriedents.addAll(table); 		
				
				System.out.println("---------------------------------------");
				System.out.println(count + ": Agent placed ingrdients in the system: "+ table);
				empty=false;
				notifyAll();
			}
		}
		
	}
	
	//make class called by chef
	public void make(String chefName, String chefIngredient)throws InterruptedException {
		while(count<21) {
			synchronized(this) {//synch this instance of the kitchen
				//wait if kitchen is empty
				while(empty) {
					wait();
				}
				//determine what chef calls this class an ingredients in kitchen
				if ((table.contains(BREAD) && (table.contains(PB) && chefIngredient==JAM))) {
					System.out.println("The " + chefName + " spreads on " + chefIngredient + " to finish the sandwich and then he eats it \n");
					empty = true;
					notifyAll();
					count++;
				} else if ((table.contains(BREAD) && (table.contains(JAM) && chefIngredient==PB))) {
					System.out.println("The " + chefName + " spreads on " + chefIngredient + " to finish the sandwich and then he eats it \n");
					empty = true;
					notifyAll();
					count++;
				} else if ((table.contains(PB) && (table.contains(JAM) && chefIngredient==PB))) {
					System.out.println("The " + chefName + " uses his " + chefIngredient + " to finish the sandwich and then he eats it \n");
					empty = true;
					notifyAll();
					count++;
				}
				
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		
		//creates kitchen, chefs and agents
		Kitchen system = new Kitchen();
		Agent agent = new Agent(system);
		chef breadMaker = new chef("breadChef", BREAD, system);
		chef PBMaker = new chef("PBChef", PB, system);
		chef JAMMaker = new chef("JamMaker", JAM, system);
		//initialize count
		count = 1;
		//start threads
		agent.start();
		breadMaker.start();
		PBMaker.start();
		JAMMaker.start();
		
	}
	

}
