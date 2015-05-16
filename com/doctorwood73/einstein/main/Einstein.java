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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jibble.jmegahal.JMegaHal;

import com.doctorwood73.einstein.chat.Chat;
import com.doctorwood73.einstein.filemanager.Brain;
import com.doctorwood73.einstein.filemanager.Config;
import com.doctorwood73.einstein.filemanager.FileUtil;
import com.doctorwood73.einstein.filemanager.NPCData;

import de.inventivegames.npc.NPC;
import de.inventivegames.npc.NPCLib;

public class Einstein extends JavaPlugin {
	
	/**List of NPC data*/
	public static ArrayList<String[]> npcListData = new ArrayList<String[]>();
	
	/**List of actual NPC's*/
	public static ArrayList<NPC> npcList = new ArrayList<NPC>();
	
	/**List of brain names/objects*/
	public static Map<String, JMegaHal> brains = new HashMap<String, JMegaHal>();
	
	/**List of actual brains*/
	public static ArrayList<JMegaHal> jmh = new ArrayList<JMegaHal>();
	
	/**Radius of NPC ears*/
	public static double ear = 8;
	
	/**How often NPC updates target*/
	public static int npcUpdateLookRate = 10000;
	
	/**Decides if NPC updates target upon starting conversation*/
	public static boolean lookOnConversationStart = false;
	
	/**Determines if NPC responds to all players within radius*/
	public static boolean respondInArea = true;
	
	/**Determines NPC's max response size*/
	public static int maxResponseSize = 150;
	
	/**The timer object that updates the looks*/
	public static Timer tim;
	
	/**Last reload time*/
	public long lastReload;
	
	public void onEnable() {
		lastReload = FileUtil.readLastReload();
		
		final long currentTime = System.currentTimeMillis();
		if(currentTime-lastReload < 45000) {
			getServer().getLogger().info("Einstein Error - Reload Can Only " +
					"Happen Every 45 Seconds, " +
					"Otherwise Skins Can't Properly Load From Network.");
			getServer().getLogger().info(
					String.valueOf(45-(int)((currentTime-lastReload)/1000))+
					" Seconds Till You Can Reload Successfully.");
			final long CUR = currentTime;
			final long LAST = lastReload;
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					Bukkit.broadcastMessage("Einstein Error - Reload Can Only " +
							"Happen Every 45 Seconds, " +
							"Otherwise Skins Can't Properly Load From Network.");
					Bukkit.broadcastMessage(
							String.valueOf(45-(int)((CUR-LAST)/1000))+
							" Seconds Till You Can Reload Successfully.");
				}
			}, 20L);
		}
		else {
			FileUtil.updateLastReload(currentTime);
		}
		
		
		Config.readData();
		new Chat(Einstein.this);
		brains = Brain.getBrains();
		npcListData = NPCData.loadNPCData();
		npcList = NPCUtil.generateNPCs(npcListData);
		new NPCLook(npcList);
		
	}
	public void onDisable() {
		NPCData.saveNPCData(npcListData);
		if(tim != null && tim.isRunning())
			tim.stop();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		Player p = (Player)sender;
		if(!p.isOp()) {
			p.sendMessage(ChatColor.GOLD + "You do not have permission " +
					"for this command.");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("Einstein")) {
			if(args.length != 2)
				return false;
			
			boolean isValidBrain = false;
			for(String s : brains.keySet()) {
				if(s.equalsIgnoreCase(args[1])) {
					isValidBrain = true;
				}
			}
			if(!isValidBrain) {
				p.sendMessage(ChatColor.GOLD + "Error; no brain by name \'" +
						ChatColor.AQUA + args[1] + ChatColor.GOLD + "\' exists.");
				p.sendMessage(ChatColor.DARK_AQUA + "Use \brains to view all existing brains.");
				return false;
			}
			NPC npc = NPCLib.spawnNPC(p.getLocation(), args[0]);
			npc.setFrozen(true);
			npcListData.add(new String[]{npc.getLocation().getWorld().getName()+","+
					String.valueOf(npc.getLocation().getX())+","+
					String.valueOf(npc.getLocation().getY())+","+
					String.valueOf(npc.getLocation().getZ()), args[0], args[1] });
			npcList.add(npc);
			p.sendMessage(ChatColor.GOLD + "Einstein " + ChatColor.DARK_AQUA +
					args[0] + ChatColor.GOLD + " successfully created.");
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("brains") ||
				cmd.getName().equalsIgnoreCase("brain")) {
			if(brains.size() == 0) {
				p.sendMessage(ChatColor.GOLD + "There are no brains installed in " +
						"Einstein\\Brains folder.");
				p.sendMessage(ChatColor.AQUA + "See <url> for more information.");
				return true;
			}
			StringBuilder brainSB = new StringBuilder();
			
			for(String s : brains.keySet()) {
				brainSB.append(s + ", ");
			}
			brainSB.deleteCharAt(brainSB.length()-1);
			brainSB.setCharAt(brainSB.length()-1, '.');
			p.sendMessage(ChatColor.DARK_AQUA + brainSB.toString());
			p.sendMessage(ChatColor.GOLD + "List of Brains Complete.");
			return true;
		}
		return false;
	}
}
