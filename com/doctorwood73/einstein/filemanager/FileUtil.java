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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	
	/**Creates the folder 'Einstein'
	 * if it doesn't exist*/
	public static void rootEinstein() {
		if(!new File("Einstein").exists())
			new File("Einstein").mkdir();
	}
	
	/**Loads the last time the server was reloaded
	 * @return milliseconds passed since jan1 1970 when reload occurred
	 */
	public static long readLastReload() {
		long lastReload = 0;
		rootEinstein();
		
		File file = new File("Einstein\\LastReload.dat");
		if(!file.exists()) {
			return 0;
		}
		
		FileInputStream fis = null;
		DataInputStream dis = null;
		try{
			fis = new FileInputStream(file);
			dis = new DataInputStream(fis);
			lastReload = dis.readLong();
		}catch(IOException e) { e.printStackTrace();
		}finally {
			try{
				if(fis != null)
					fis.close();
			}catch(IOException e) { e.printStackTrace(); }
			try{
				if(dis != null)
					dis.close();
			}catch(IOException e) { e.printStackTrace(); }
		}
		return lastReload;
	}
	
	/**Updates the last reload time*/
	public static void updateLastReload(long time) {
		rootEinstein();
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try{
			File file = new File("Einstein\\LastReload.dat");
			if(file.exists())
				file.delete();
			fos = new FileOutputStream(file);
			dos = new DataOutputStream(fos);
			dos.writeLong(time);
		}catch(IOException e) { e.printStackTrace();
		}finally {
			try{
				if(fos != null)
					fos.close();
			}catch(IOException e) { e.printStackTrace(); }
			try{
				if(dos != null)
					dos.close();
			}catch(IOException e) { e.printStackTrace(); }
		}
	}
	
}
