/*
 * Dungeons Guide - The most intelligent Hypixel Skyblock Dungeons Mod
 * Copyright (C) 2021  cyoung06
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

package kr.syeyoung.dungeonsguide.dungeon.mechanics;

import com.google.common.collect.Sets;
import kr.syeyoung.dungeonsguide.dungeon.data.OffsetPoint;
import kr.syeyoung.dungeonsguide.dungeon.mechanics.dunegonmechanic.DungeonMechanic;
import kr.syeyoung.dungeonsguide.dungeon.mechanics.predicates.PredicateArmorStand;
import kr.syeyoung.dungeonsguide.mod.dungeon.actions.AbstractAction;
import kr.syeyoung.dungeonsguide.mod.dungeon.actions.ActionChangeState;
import kr.syeyoung.dungeonsguide.mod.dungeon.actions.ActionInteract;
import kr.syeyoung.dungeonsguide.mod.dungeon.actions.ActionMove;
import kr.syeyoung.dungeonsguide.mod.dungeon.roomfinder.DungeonRoom;
import kr.syeyoung.dungeonsguide.mod.utils.RenderUtils;
import lombok.Data;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Data
public class DungeonWizard implements DungeonMechanic {
    private static final long serialVersionUID = -89487601113028763L;
    private OffsetPoint secretPoint = new OffsetPoint(0, 0, 0);
    private List<String> preRequisite = new ArrayList<String>();
    private String crystal = "";


    @Override
    public Set<AbstractAction> getAction(String state, DungeonRoom dungeonRoom) {
        if (!"navigate".equalsIgnoreCase(state) && !"quest".equalsIgnoreCase(state) && !"click".equalsIgnoreCase(state))
            throw new IllegalArgumentException(state + " is not a valid state for secret");

        Set<AbstractAction> base;
        Set<AbstractAction> realbase = base = new HashSet<>();
        if ("click".equalsIgnoreCase(state) || "quest".equalsIgnoreCase(state)) {
            ActionInteract actionClick = new ActionInteract(secretPoint);
            actionClick.setPredicate(a -> a instanceof EntityOtherPlayerMP);
            actionClick.setRadius(3);
            base.add(actionClick);
            base = actionClick.getPreRequisite();
        }

        ActionMove actionMove = new ActionMove(secretPoint);
        base.add(actionMove);
        base = actionMove.getPreRequisite();

        for (String str : preRequisite) {
            if (!str.isEmpty()) {
                String[] split = str.split(":");
                base.add(new ActionChangeState(split[0], split[1]));
            }
        }

        if ("quest".equalsIgnoreCase(state)) {
            base.add(new ActionChangeState(crystal, "obtained-self"));
        }
        System.out.println(realbase);
        return realbase;
    }

    @Override
    public void highlight(Color color, String name, DungeonRoom dungeonRoom, float partialTicks) {
        BlockPos pos = getSecretPoint().getBlockPos(dungeonRoom);
        RenderUtils.highlightBlock(pos, color, partialTicks);
        RenderUtils.drawTextAtWorld("W-" + name, pos.getX() + 0.5f, pos.getY() + 0.375f, pos.getZ() + 0.5f, 0xFFFFFFFF, 0.03f, false, true, partialTicks);
        RenderUtils.drawTextAtWorld(getCurrentState(dungeonRoom), pos.getX() + 0.5f, pos.getY() + 0f, pos.getZ() + 0.5f, 0xFFFFFFFF, 0.03f, false, true, partialTicks);
    }


    public DungeonWizard clone() throws CloneNotSupportedException {
        DungeonWizard dungeonSecret = new DungeonWizard();
        dungeonSecret.secretPoint = (OffsetPoint) secretPoint.clone();
        dungeonSecret.crystal = crystal;
        dungeonSecret.preRequisite = new ArrayList<String>(preRequisite);
        return dungeonSecret;
    }


    @Override
    public String getCurrentState(DungeonRoom dungeonRoom) {
        if (dungeonRoom.getRoomContext().containsKey("wizardcrystal")) {
            return "quest";
        }
        return "no-state";
    }

    @Override
    public Set<String> getPossibleStates(DungeonRoom dungeonRoom) {
        if (crystal != null) {
            DungeonMechanic crystal1 = dungeonRoom.getMechanics().get(crystal);
            String crystalState = crystal1.getCurrentState(dungeonRoom);
            if (crystalState.equalsIgnoreCase("obtained-other")) {
                return Sets.newHashSet("navigate", "click");
            }
        }

        return getCurrentState(dungeonRoom).equalsIgnoreCase("quest") ?
                Sets.newHashSet("navigate", "click") : Sets.newHashSet("navigate", "click", "quest");
    }

    @Override
    public Set<String> getTotalPossibleStates(DungeonRoom dungeonRoom) {
        return Sets.newHashSet("no-state", "click", "quest", "interact");
    }

    @Override
    public OffsetPoint getRepresentingPoint(DungeonRoom dungeonRoom) {
        return secretPoint;
    }
}
