package com.softwaremagico.tm.json;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *  
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *  
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.values.IValue;

public class CharacterJsonManager {

	public static String toJson(CharacterPlayer characterPlayer) {
		if (characterPlayer != null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			gsonBuilder.registerTypeAdapter(IValue.class, new IValueSerializer<IValue>());
			// final Gson gson = new
			// GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
			Gson gson = gsonBuilder.create();
			String jsonText = gson.toJson(characterPlayer);
			return jsonText;
		}
		return null;
	}

	public static CharacterPlayer fromJson(String jsonText) {
		if (jsonText != null && jsonText.length() > 0) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			gsonBuilder.registerTypeAdapter(IValue.class, new InterfaceAdapter<IValue>());
			Gson gson = gsonBuilder.create();

			CharacterPlayer characterPlayer = gson.fromJson(jsonText, CharacterPlayer.class);
			return characterPlayer;
		}
		return null;
	}

	public static CharacterPlayer fromFile(String path) throws IOException {
		String jsonText = new String(Files.readAllBytes(Paths.get(path)));
		return fromJson(jsonText);
	}

	private static class IValueSerializer<T> implements JsonSerializer<T> {
		@Override
		public JsonElement serialize(T link, Type type, JsonSerializationContext context) {
			return context.serialize(link, link.getClass());
		}
	}

	private static class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

		private static final String JSON_CLASSNAME = "classname";
		private static final String JSON_DATA = "data";

		@Override
		public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
				throws JsonParseException {

			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonPrimitive prim = (JsonPrimitive) jsonObject.get(JSON_CLASSNAME);
			if (prim == null) {
				return null;
			}
			String className = prim.getAsString();
			return jsonDeserializationContext.deserialize(jsonObject.get(JSON_DATA), getObjectClass(className));
		}

		@Override
		public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(JSON_CLASSNAME, jsonElement.getClass().getName());
			jsonObject.add(JSON_DATA, jsonSerializationContext.serialize(jsonElement));
			return jsonObject;
		}

		public Class<?> getObjectClass(String className) {
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new JsonParseException(e.getMessage());
			}
		}
	}
}
