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

package com.doctorwood73.einstein.filemanager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.doctorwood73.einstein.main.Einstein;

public class Config {
	/**config file*/
	public static String configFile = "config-0-0-1.txt";
	
	/**Reads in data from config*/
	public static void readData() {
		FileUtil.rootEinstein();
		File file = new File("Einstein\\" + configFile);
		if(!file.exists()) {
			PrintWriter pw = null;
			try{
				pw = new PrintWriter(file);
				pw.println("NPC Listen Radius: 8.0");
				Einstein.ear = 8;
				pw.flush();
				pw.println("NPC Look At Nearby Player Update Rate in ms (negative=no computation - may help possible lag): 10000");
				Einstein.npcUpdateLookRate = 10000;
				pw.flush();
				pw.println("NPC Looks At Player When Conversation Starts(less computation solution): false");
				Einstein.lookOnConversationStart = false;
				pw.flush();
				pw.println("NPC Responds To All Players In Listen Radius(adds computation): true");
				Einstein.respondInArea = true;
				pw.flush();
				pw.println("NPC max character response count: 150");
				Einstein.maxResponseSize = 150;
				pw.flush();
			}catch(IOException e) { e.printStackTrace();
			}finally {
				if(pw != null)
					pw.close();
			}
		}
		else {
			Scanner in = null;
			try{
				in = new Scanner(file);
				String line1 = in.nextLine();
				Einstein.ear = Double.parseDouble(
						line1.substring(line1.indexOf(":")+1).trim());
				String line2 = in.nextLine();
				Einstein.npcUpdateLookRate = Integer.parseInt(
						line2.substring(line2.indexOf(":")+1).trim());
				String line3 = in.nextLine();
				Einstein.lookOnConversationStart = line3.substring(
						line3.indexOf(":")+1).trim().equalsIgnoreCase("true") ?
							true : false;
				String line4 = in.nextLine();
				Einstein.respondInArea = line4.substring(
						line4.indexOf(":")+1).trim().equalsIgnoreCase("true") ?
								true : false;
				String line5 = in.nextLine();
				Einstein.maxResponseSize = Integer.parseInt(
						line5.substring(line5.indexOf(":")+1).trim());
			}catch(IOException e) { e.printStackTrace();
			}finally {
				if(in != null)
					in.close();
			}
		}
	}
}
