/*
 *     Dungeons Guide - The most intelligent Hypixel Skyblock Dungeons Mod
 *     Copyright (C) 2021  cyoung06
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package kr.syeyoung.dungeonsguide.config.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class TCString implements TypeConverter<String> {
    @Override
    public String getTypeString() {
        return "string";
    }

    @Override
    public String deserialize(JsonElement element) {
        return element.getAsString();
    }

    @Override
    public JsonElement serialize(String element) {
        return new JsonPrimitive(element);
    }
}
