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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jibble.jmegahal.JMegaHal;


public class Brain {
	
	/**Loads all brains
	 * @return Map<String, JMegaHal> of brains
	 */
	public static Map<String, JMegaHal> getBrains() {
		FileUtil.rootEinstein();
		if(!new File("Einstein\\Brains").exists())
			new File("Einstein\\Brains").mkdir();
		File[] brainList = new File("Einstein\\Brains").listFiles();
		ArrayList<String> brainAl = new ArrayList<String>();
		ArrayList<JMegaHal> jmhAl = new ArrayList<JMegaHal>();
		for(File f : brainList) {
			CharSequence cs = ".";
			String name = f.getName();
			if(name.contains(cs)) {
				if(name.charAt(name.length()-1) != '.') {
					if(name.substring(name.lastIndexOf(".")+1).equalsIgnoreCase(
							"ser")) {
						brainAl.add(f.getName().substring(0, f.getName().indexOf(".")));
						FileInputStream fis = null;
						ObjectInputStream ois = null;
						try {
							fis = new FileInputStream(f);
							ois = new ObjectInputStream(fis);
							JMegaHal hal = (JMegaHal) ois.readObject();
							jmhAl.add(hal);
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
					}
				}
			}
		}
		Map<String, JMegaHal> map = new HashMap<String, JMegaHal>();
		for(int i = 0; i < brainAl.size(); i++) {
			map.put(brainAl.get(i), jmhAl.get(i));
		}
		return map;
	}
	
}
