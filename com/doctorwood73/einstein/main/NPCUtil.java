/*
 * Copyright 2015 CrystalCraftMC
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

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.inventivegames.npc.NPC;
import de.inventivegames.npc.NPCLib;

public class NPCUtil {
	
	/**Loads NPC data into real NPC's
	 * @return ArrayList<NPC> of generated npc's
	 */
	public static ArrayList<NPC> generateNPCs(ArrayList<String[]> npcListData) {
		ArrayList<NPC> npcList = new ArrayList<NPC>();
		for(int i = 0; i < npcListData.size(); i++) {
			String[] locData = npcListData.get(i)[0].split(",");
			npcList.add(NPCLib.spawnNPC(
					new Location(Bukkit.getWorld(locData[0]), Double.parseDouble(locData[1]), 
							Double.parseDouble(locData[2]), Double.parseDouble(locData[3])),
					npcListData.get(i)[1]));
			
		}
		return npcList;
	}
}
