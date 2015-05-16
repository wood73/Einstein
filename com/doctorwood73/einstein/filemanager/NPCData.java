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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class NPCData {
	
	/**Saves npc data to .ser file
	 * @param al, an ArrayList of String arrays; the data we're saving
	 */
	public static void saveNPCData(ArrayList<String[]> al) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			FileUtil.rootEinstein();
			File file = new File("Einstein\\NPCData.ser");
			if(file.exists())
				file.delete();
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(al);
			
		}catch(IOException e) { e.printStackTrace(); 
		}finally {
			try{
				if(fos != null)
					fos.close();
			}catch(IOException e) { e.printStackTrace(); }
			try{
				if(oos != null)
					oos.close();
			}catch(IOException e) { e.printStackTrace(); }
		}
	}
	
	/**Loads NPC data
	 * @return an arraylist of string arrays
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String[]> loadNPCData() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		ArrayList<String[]> npcData = new ArrayList<String[]>();
		FileUtil.rootEinstein();
		if(!new File("Einstein\\NPCData.ser").exists())
			return npcData;
		
		try {
			File file = new File("Einstein\\NPCData.ser");
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			npcData = (ArrayList<String[]>) ois.readObject();
		}catch(IOException e) { e.printStackTrace();
		}catch(ClassNotFoundException e) { e.printStackTrace();
		}finally {
			try{
				if(fis != null)
					fis.close();
			}catch(IOException e) { e.printStackTrace(); }
			try{
				if(ois != null)
					ois.close();
			}catch(IOException e) { e.printStackTrace(); }
		}
		
		return npcData;
	}
	
}
