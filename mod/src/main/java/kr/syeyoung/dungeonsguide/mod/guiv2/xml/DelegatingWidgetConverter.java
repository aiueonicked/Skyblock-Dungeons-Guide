/*
 * Dungeons Guide - The most intelligent Hypixel Skyblock Dungeons Mod
 * Copyright (C) 2023  cyoung06 (syeyoung)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package kr.syeyoung.dungeonsguide.mod.guiv2.xml;

import kr.syeyoung.dungeonsguide.mod.guiv2.BindableAttribute;
import kr.syeyoung.dungeonsguide.mod.guiv2.Widget;
import kr.syeyoung.dungeonsguide.mod.guiv2.xml.data.ParserElement;
import kr.syeyoung.dungeonsguide.mod.guiv2.xml.data.ParserElementList;
import kr.syeyoung.dungeonsguide.mod.guiv2.xml.data.WidgetList;
import net.minecraft.util.ResourceLocation;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DelegatingWidgetConverter<R extends Widget & ImportingWidget> extends PropByPropParsedWidgetConverter<DelegatingWidget, R> {
    private final ResourceLocation resourceLocation;
    public DelegatingWidgetConverter(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public DelegatingWidget instantiateWidget() {
        return new DelegatingWidget(resourceLocation);
    }

    @Override
    public BindableAttribute getExportedAttribute(DelegatingWidget widget, String attributeName) {
        return widget.getExportedAttribute(attributeName);
    }

}
