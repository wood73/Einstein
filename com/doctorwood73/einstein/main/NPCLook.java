/*
 * Copyright 2015 Alex Woodward
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.doctorwood73.einstein.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.inventivegames.npc.NPC;

/**This class will enable npc's to look at nearby players*/
public class NPCLook {
	
	
	/**List of npc's to update look for*/
	ArrayList<NPC> updateLook = new ArrayList<NPC>();
	
	public NPCLook(ArrayList<NPC> updateLook) {
		this.updateLook = updateLook;
		if(Einstein.npcUpdateLookRate >= 1) {
			Einstein.tim = new Timer(Einstein.npcUpdateLookRate, new UpdateLookTim());
			Einstein.tim.start();
		}
	}
	
	private class UpdateLookTim implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < updateLook.size(); i++) {
				Player nearest = null;
				double lowest = 200000;
				for(Player test : Bukkit.getServer().getOnlinePlayers()) {
					if(!test.getWorld().getName().equals(
							updateLook.get(i).getLocation().getWorld().getName())) {
						continue;
					}
					if(nearest == null) {
						nearest = test;
						lowest = Math.sqrt(
								(Math.pow((test.getLocation().getX(
										)-updateLook.get(i).getLocation(
												).getX()), 2))+
								(Math.pow((test.getLocation().getY(
										)-updateLook.get(i).getLocation(
												).getY()), 2))+
								(Math.pow((test.getLocation().getZ(
										)-updateLook.get(i).getLocation(
												).getZ()), 2)));
						continue;
					}
					double distance = Math.sqrt(
							(Math.pow((test.getLocation().getX(
									)-updateLook.get(i).getLocation(
											).getX()), 2))+
							(Math.pow((test.getLocation().getY(
									)-updateLook.get(i).getLocation(
											).getY()), 2))+
							(Math.pow((test.getLocation().getZ(
									)-updateLook.get(i).getLocation(
											).getZ()), 2)));
					if(distance < lowest) {
						lowest = distance;
						nearest = test;
					}
				}
				if(nearest != null)
					updateLook.get(i).setTarget(nearest);
			}
		}
	}

}
