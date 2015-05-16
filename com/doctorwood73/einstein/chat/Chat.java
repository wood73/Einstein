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

package com.doctorwood73.einstein.chat;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jibble.jmegahal.JMegaHal;

import com.doctorwood73.einstein.main.Einstein;

public class Chat implements Listener {
	private Einstein plugin;
	
	public Chat(Einstein plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	/**Initiate a response between you/(nearby players) and the NPC's brain if in range*/
	@EventHandler
	public void conversation(AsyncPlayerChatEvent e) {
		int npcIndex = getNPCIndex(e.getPlayer().getLocation());
		if(npcIndex == -1)
			return;
		
		Player p = e.getPlayer();
		if(Einstein.lookOnConversationStart)
			Einstein.npcList.get(npcIndex).setTarget(p);
		
		String response = Chat.generateResponse(npcIndex, e.getMessage());
		
		int delay = 40;
		for(Player listener : Chat.generateResponseList(npcIndex, p)) {
			fireResponse(listener, npcIndex, response, delay);
		}
	}
	
	/**Sends a response from NPC to player
	 * @param player we're sending to
	 * @param int NPC index we're sending from
	 * @param String the response
	 * @param int delay (ticks) to send
	 */
	public void fireResponse(final Player P, final int NPCINDEX,
			final String RESPONSE, final int DELAY) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				P.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA +
						Einstein.npcListData.get(NPCINDEX)[1] + ChatColor.DARK_GRAY +
						"]:" + ChatColor.GOLD + " " + RESPONSE);
			}
		}, (long)DELAY);
	}
	
	/**Gets closest npc index if any; returns -1 if none found in Einstein.ear radius
	 * @param Location location we're checking
	 * @return npc index of Einstein.npcList && Einstein.npcListData within Einstein.ear of location
	 */
	public static int getNPCIndex(Location loc) {
		int lowestIndex = -1;
		double lowest = 2000000;
		for(int i = 0; i < Einstein.npcList.size(); i++) {
			if(!loc.getWorld().getName().equals(
					Einstein.npcList.get(i).getLocation().getWorld().getName())) {
				continue;
			}
			double distance = Math.sqrt(
					(Math.pow((loc.getX(
							)-Einstein.npcList.get(i).getLocation(
									).getX()), 2))+
					(Math.pow((loc.getY(
							)-Einstein.npcList.get(i).getLocation(
									).getY()), 2))+
					(Math.pow((loc.getZ(
							)-Einstein.npcList.get(i).getLocation(
									).getZ()), 2)));
			if(distance < Einstein.ear && distance < lowest) {
				lowest = distance;
				lowestIndex = i;
			}
		}
		return lowestIndex;
	}
	
	/**Gets all players within an Einstein.ear block radius
	 * of the NPC addressed in the conversation
	 * @param int npcIndex
	 * @return ArrayList<Player> of those in ear range
	 */
	public static ArrayList<Player> generateResponseList(int npcIndex, Player talker) {
		ArrayList<Player> responseList = new ArrayList<Player>();
		if(Einstein.respondInArea) {
			for(Player test : Bukkit.getServer().getOnlinePlayers()) {
				if(!test.getWorld().getName().equals(
						Einstein.npcList.get(npcIndex).getLocation().getWorld().getName())) {
					continue;
				}
				Location loc = test.getLocation();
				double distance = Math.sqrt(
					(Math.pow((loc.getX(
							)-Einstein.npcList.get(npcIndex).getLocation(
									).getX()), 2))+
					(Math.pow((loc.getY(
							)-Einstein.npcList.get(npcIndex).getLocation(
									).getY()), 2))+
					(Math.pow((loc.getZ(
							)-Einstein.npcList.get(npcIndex).getLocation(
									).getZ()), 2)));
				if(distance < Einstein.ear) {
					responseList.add(test);
				}
			}
		}
		else {
			responseList.add(talker);
		}
		return responseList;
	}
	
	/**Generates a response given an NPC and an input
	 * @param int npcIndex, the npc we're referring to
	 * @param String message, the message to send the NPC
	 * @return String, the response given by the NPC
	 */
	public static String generateResponse(int npcIndex, String msg) {
		JMegaHal brain = Einstein.brains.get(
				Einstein.npcListData.get(npcIndex)[2]);
		String[] keyword = msg.split(" ");
		String s = msg;
		s = keyword.length >= 1 ? keyword[0] : s;
		String response = brain.getSentence(s);
		
		if(response.length() > Einstein.maxResponseSize) {
			response = response.substring(0, Einstein.maxResponseSize+1);
		}
		if(response.charAt(response.length()-1) != '?' &&
				response.charAt(response.length()-1) != '!' &&
						response.charAt(response.length()-1) != '.') {
			response = response + ".";
		}
		return response;
	}
}
