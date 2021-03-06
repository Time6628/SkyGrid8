package funwayguy.skygrid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.Level;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import funwayguy.skygrid.core.SkyGrid;

/**
 * Used to read JSON data with pre-made checks for null entries and casting.</br>
 * In the event the requested value is missing, it will be added to the JSON object
 */
public class JsonHelper
{
	public static JsonArray GetArray(JsonObject json, String id)
	{
		if(json == null)
		{
			return new JsonArray();
		}
		
		if(json.has(id) && json.get(id).isJsonArray())
		{
			return json.get(id).getAsJsonArray();
		} else
		{
			JsonArray array = new JsonArray();
			json.add(id, array);
			return array;
		}
	}
	
	public static JsonObject GetObject(JsonObject json, String id)
	{
		if(json == null)
		{
			return new JsonObject();
		}
		
		if(json.has(id) && json.get(id).isJsonObject())
		{
			return json.get(id).getAsJsonObject();
		} else
		{
			JsonObject obj = new JsonObject();
			json.add(id, obj);
			return obj;
		}
	}
	
	public static String GetString(JsonObject json, String id, String def)
	{
		if(json == null)
		{
			return def;
		}
		
		if(json.has(id) && json.get(id).isJsonPrimitive() && json.get(id).getAsJsonPrimitive().isString())
		{
			return json.get(id).getAsString();
		} else
		{
			JsonPrimitive prim = new JsonPrimitive(def);
			json.add(id, prim);
			return def;
		}
	}
	
	public static Number GetNumber(JsonObject json, String id, Number def)
	{
		if(json == null)
		{
			return def;
		}
		
		if(json.has(id) && json.get(id).isJsonPrimitive() && json.get(id).getAsJsonPrimitive().isNumber())
		{
			return json.get(id).getAsInt();
		} else
		{
			JsonPrimitive prim = new JsonPrimitive(def);
			json.add(id, prim);
			return def;
		}
	}
	
	public static boolean GetBoolean(JsonObject json, String id, boolean def)
	{
		if(json == null)
		{
			return def;
		}
		
		if(json.has(id) && json.get(id).isJsonPrimitive())
		{
			try // Booleans can be stored as strings so there is no quick way of determining whether it is valid or not
			{
				return json.get(id).getAsBoolean();
			} catch(Exception e)
			{
				JsonPrimitive prim = new JsonPrimitive(def);
				json.add(id, prim);
				return def;
			}
		} else
		{
			JsonPrimitive prim = new JsonPrimitive(def);
			json.add(id, prim);
			return def;
		}
	}
	
	public static JsonObject ReadObjectFromFile(File file)
	{
		if(!file.exists())
		{
			return new JsonObject();
		}
		
		try
		{
			InputStreamReader fr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
			JsonObject json = new Gson().fromJson(fr, JsonObject.class);
			fr.close();
			return json;
		} catch(Exception e)
		{
			SkyGrid.logger.log(Level.ERROR, "An error occured while loading JSON from file:", e);
			return new JsonObject(); // Just a safety measure against NPEs
		}
	}
	
	public static JsonArray ReadArrayFromFile(File file)
	{
		if(!file.exists())
		{
			return new JsonArray();
		}
		
		try
		{
			InputStreamReader fr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
			JsonArray json = new Gson().fromJson(fr, JsonArray.class);
			fr.close();
			return json;
		} catch(Exception e)
		{
			SkyGrid.logger.log(Level.ERROR, "An error occured while loading JSON from file:", e);
			return new JsonArray(); // Just a safety measure against NPEs
		}
	}
	
	public static void WriteToFile(File file, JsonElement json)
	{
		try
		{
			if(!file.exists())
			{
				if(file.getParentFile() != null)
				{
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
			new GsonBuilder().setPrettyPrinting().create().toJson(json, fw);
			fw.close();
		} catch(Exception e)
		{
			SkyGrid.logger.log(Level.ERROR, "An error occured while saving JSON to file:", e);
			return;
		}
	}
}
