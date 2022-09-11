package com.ferox.game.content.items.combine;

import com.ferox.fs.ItemDefinition;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;

import java.util.Arrays;
import java.util.List;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 28, 2020
 */
public class ItemCombination extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        ItemDefinition def = item.definition(World.getWorld());
        if(option == 1) {
            if(item.getId() == VORKATHS_HEAD_21907) {
                player.message("This blue dragon smells like it's been dead for a remarkably long time. Even by my...");
                player.message("standards, it smells awful.");
                return true;
            }
        }
        if(option == 2) {
            if(def != null && def.name.contains("Twisted slayer")) {
                if(item.getId() == TWISTED_SLAYER_HELMET_I) {
                    player.message("You can't disassemble this helmet.");
                    return true;
                }
                player.optionsTitled("Are you sure you want to remove the attachment", "Remove attachment. ("+ Color.RED.wrap("attachment will be lost")+")", "Nevermind.", () -> {
                    if(player.inventory().contains(item.getId())) {
                        player.inventory().remove(item);
                        player.inventory().add(new Item(TWISTED_SLAYER_HELMET_I));
                    }
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == TWISTED_ANCESTRAL_COLOUR_KIT || usedWith.getId() == TWISTED_ANCESTRAL_COLOUR_KIT) && (use.getId() == ANCESTRAL_HAT || usedWith.getId() == ANCESTRAL_HAT)) {
            combineTwistedAncestral(player);
            return true;
        }

        if ((use.getId() == TWISTED_ANCESTRAL_COLOUR_KIT || usedWith.getId() == TWISTED_ANCESTRAL_COLOUR_KIT) && (use.getId() == ANCESTRAL_ROBE_TOP || usedWith.getId() == ANCESTRAL_ROBE_TOP)) {
            combineTwistedAncestral(player);
            return true;
        }

        if ((use.getId() == TWISTED_ANCESTRAL_COLOUR_KIT || usedWith.getId() == TWISTED_ANCESTRAL_COLOUR_KIT) && (use.getId() == ANCESTRAL_ROBE_BOTTOM || usedWith.getId() == ANCESTRAL_ROBE_BOTTOM)) {
            combineTwistedAncestral(player);
            return true;
        }

        if ((use.getId() == VORKATHS_HEAD_21907 || usedWith.getId() == VORKATHS_HEAD_21907) && (use.getId() == AVAS_ACCUMULATOR || usedWith.getId() == AVAS_ACCUMULATOR)) {
            combineAssembler(player);
            return true;
        }

        if ((use.getId() == VORKATHS_HEAD_21907 || usedWith.getId() == VORKATHS_HEAD_21907) && (use.getId() == DRAGON_HUNTER_CROSSBOW || usedWith.getId() == DRAGON_HUNTER_CROSSBOW)) {
            combineDragonHunterCrossbow(player);
            return true;
        }
        if ((use.getId() == KBD_HEADS || usedWith.getId() == KBD_HEADS) && (use.getId() == SLAYER_HELMET || usedWith.getId() == SLAYER_HELMET)) {
            combineBlackSlayerHelm(player);
            return true;
        }
        if ((use.getId() == KQ_HEAD || usedWith.getId() == KQ_HEAD) && (use.getId() == SLAYER_HELMET || usedWith.getId() == SLAYER_HELMET)) {
            combineGreenSlayerHelm(player);
            return true;
        }

        if ((use.getId() == ABYSSAL_HEAD || usedWith.getId() == ABYSSAL_HEAD) && (use.getId() == SLAYER_HELMET || usedWith.getId() == SLAYER_HELMET)) {
            combineRedSlayerHelm(player);
            return true;
        }
        if ((use.getId() == VORKATHS_HEAD_21907 || usedWith.getId() == VORKATHS_HEAD_21907) && (use.getId() == SLAYER_HELMET || usedWith.getId() == SLAYER_HELMET)) {
            combineTurSlayerHelm(player);
            return true;
        }

        if ((use.getId() == KBD_HEADS || usedWith.getId() == KBD_HEADS) && (use.getId() == TWISTED_SLAYER_HELMET_I || usedWith.getId() == TWISTED_SLAYER_HELMET_I)) {
            combineTwistedKBD(player);
            return true;
        }

        if ((use.getId() == BLOOD_MONEY || usedWith.getId() == BLOOD_MONEY) && (use.getId() == TWISTED_SLAYER_HELMET_I || usedWith.getId() == TWISTED_SLAYER_HELMET_I)) {
            combineTwistedJad(player);
            return true;
        }

        if ((use.getId() == INFERNAL_CAPE || usedWith.getId() == INFERNAL_CAPE) && (use.getId() == TWISTED_SLAYER_HELMET_I || usedWith.getId() == TWISTED_SLAYER_HELMET_I)) {
            combineTwistedZuk(player);
            return true;
        }

        if ((use.getId() == VAMPYRE_DUST || usedWith.getId() == VAMPYRE_DUST) && (use.getId() == TWISTED_SLAYER_HELMET_I || usedWith.getId() == TWISTED_SLAYER_HELMET_I)) {
            combineTwistedVamp(player);
            return true;
        }

        if ((use.getId() == SAELDOR_SHARD || usedWith.getId() == SAELDOR_SHARD) && (use.getId() == BLADE_OF_SAELDOR || usedWith.getId() == BLADE_OF_SAELDOR)) {
            combineBladeOfSaeldor(player);
            return true;
        }
        if ((use.getId() == SHADOW_INQUISITOR_ORNAMENT_KIT || usedWith.getId() == SHADOW_INQUISITOR_ORNAMENT_KIT) && (use.getId() == INQUISITORS_MACE || usedWith.getId() == INQUISITORS_MACE)) {
            combineShadowMace(player);//updatet3
            return true;
        }

        if ((use.getId() == DARK_CLAW || usedWith.getId() == DARK_CLAW) && (use.getId() == TWISTED_SLAYER_HELMET_I || usedWith.getId() == TWISTED_SLAYER_HELMET_I)) {
            combineTwistedSkot(player);
            return true;
        }

        if ((use.getId() == ALCHEMICAL_HYDRA_HEADS || usedWith.getId() == ALCHEMICAL_HYDRA_HEADS) && (use.getId() == TWISTED_SLAYER_HELMET_I || usedWith.getId() == TWISTED_SLAYER_HELMET_I)) {
            combineTwistedHydra(player);
            return true;
        }

        if ((use.getId() == TWISTED_BOW || usedWith.getId() == TWISTED_BOW) && (use.getId() == TWISTED_BOW_KIT || usedWith.getId() == TWISTED_BOW_KIT)) {
            combineTwistedBow(player);
            return true;
        }

        if ((use.getId() == SCYTHE_OF_VITUR || usedWith.getId() == SCYTHE_OF_VITUR) && (use.getId() == SCYTHE_OF_VITUR_KIT || usedWith.getId() == SCYTHE_OF_VITUR_KIT)) {
            combineScytheOfVitur(player);
            return true;
        }

        if ((use.getId() == BANDOS_BOOTS || usedWith.getId() == BANDOS_BOOTS) && (use.getId() == BLACK_TOURMALINE_CORE || usedWith.getId() == BLACK_TOURMALINE_CORE)) {
            combineGuardianBoots(player);
            return true;
        }
        List<Integer> dkite_products = Arrays.asList(DRAGON_METAL_SHARD, DRAGON_METAL_SLICE);
        for (int id : dkite_products) {
            if ((use.getId() == id || usedWith.getId() == id) && (use.getId() == DRAGON_SQ_SHIELD || usedWith.getId() == DRAGON_SQ_SHIELD)) {
                createDragonKiteshield(player);
                return true;
            }
        }

        List<Integer> dplate_products = Arrays.asList(DRAGON_METAL_SHARD, DRAGON_METAL_LUMP);
        for (int id : dplate_products) {
            if ((use.getId() == id || usedWith.getId() == id) && (use.getId() == DRAGON_CHAINBODY || usedWith.getId() == DRAGON_CHAINBODY)) {
                createDragonPlatebody(player);
                return true;
            }
        }
        return false;
    }
    private void combineShadowMace(Player player) {//updatet3
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create Shadow Mace?", "Yes, sacrifice inquisitor mace & kit.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(SHADOW_INQUISITOR_ORNAMENT_KIT, INQUISITORS_MACE)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(SHADOW_INQUISITOR_ORNAMENT_KIT), true);
                        player.inventory().remove(new Item(INQUISITORS_MACE), true);
                        player.inventory().add(new Item(SHADOW_MACE), true);
                        player.message("You used shadow inquisitor ornament kit with inquisitor mace and crated shadow mace.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }
    private void combineTwistedAncestral(Player player) {
        if(!player.inventory().containsAll(new Item(ANCESTRAL_HAT), new Item(ANCESTRAL_ROBE_TOP), new Item(ANCESTRAL_ROBE_BOTTOM))) {
            player.message("You need all three ancestral pieces to combine the kit.");
            return;
        }
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create Twisted Ancestral?", "Yes, sacrifice the kit.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(new Item(ANCESTRAL_HAT), new Item(ANCESTRAL_ROBE_TOP), new Item(ANCESTRAL_ROBE_BOTTOM))) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(TWISTED_ANCESTRAL_COLOUR_KIT),true);
                        player.inventory().remove(new Item(ANCESTRAL_HAT),true);
                        player.inventory().remove(new Item(ANCESTRAL_ROBE_TOP),true);
                        player.inventory().remove(new Item(ANCESTRAL_ROBE_BOTTOM),true);
                        player.inventory().add(new Item(TWISTED_ANCESTRAL_HAT),true);
                        player.inventory().add(new Item(TWISTED_ANCESTRAL_ROBE_TOP),true);
                        player.inventory().add(new Item(TWISTED_ANCESTRAL_ROBE_BOTTOM),true);
                        player.message("You carefully attach the kit to the ancestral piece, to change its colours.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineAssembler(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create Ava's Assembler?", "Yes, sacrifice Vorkath's head.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(VORKATHS_HEAD_21907, AVAS_ACCUMULATOR)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(VORKATHS_HEAD_21907), true);
                        player.inventory().remove(new Item(AVAS_ACCUMULATOR), true);
                        player.inventory().add(new Item(AVAS_ASSEMBLER), true);
                        player.message("You carefully attach the Vorkath's head to the device and create the assembler.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineDragonHunterCrossbow(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create Dragon Hunter Crossbow (t)?", "Yes, sacrifice Vorkath's head.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(VORKATHS_HEAD_21907, DRAGON_HUNTER_CROSSBOW)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(VORKATHS_HEAD_21907), true);
                        player.inventory().remove(new Item(DRAGON_HUNTER_CROSSBOW), true);
                        player.inventory().add(new Item(DRAGON_HUNTER_CROSSBOW_T), true);
                        player.message("You carefully attach the Vorkath's head to the device and create the dragon hunter");
                        player.message("crossbow (t).");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }
    private void combineBlackSlayerHelm(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create Black Slayer helm?", "Yes, sacrifice KBD Head.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(KBD_HEADS, SLAYER_HELMET)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(KBD_HEADS), true);
                        player.inventory().remove(new Item(SLAYER_HELMET), true);
                        player.inventory().add(new Item(BLACK_SLAYER_HELMET), true);
                        player.message("You carefully attach the kbd head to the device and create the Black Slayer helm");
                        player.message("Black Slayer helm.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineRedSlayerHelm(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create Red Slayer helm?", "Yes, sacrifice Abyssal Head.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(ABYSSAL_HEAD, SLAYER_HELMET)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(ABYSSAL_HEAD), true);
                        player.inventory().remove(new Item(SLAYER_HELMET), true);
                        player.inventory().add(new Item(RED_SLAYER_HELMET), true);
                        player.message("You carefully attach the Abyssal head to the device and create the Red Slayer helm");
                        player.message("Red Slayer helm.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }
    private void combineGreenSlayerHelm(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create Green Slayer helm?", "Yes, sacrifice KQ Head.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(KQ_HEAD, SLAYER_HELMET)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(KQ_HEAD), true);
                        player.inventory().remove(new Item(SLAYER_HELMET), true);
                        player.inventory().add(new Item(GREEN_SLAYER_HELMET), true);
                        player.message("You carefully attach the Abyssal head to the device and create the Green Slayer helm");
                        player.message("Green Slayer helm.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }
    private void combineTurSlayerHelm(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create Turquoise Slayer helm?", "Yes, sacrifice Vorkath Head.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(VORKATHS_HEAD_21907, SLAYER_HELMET)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(VORKATHS_HEAD_21907), true);
                        player.inventory().remove(new Item(SLAYER_HELMET), true);
                        player.inventory().add(new Item(TURQUOISE_SLAYER_HELMET), true);
                        player.message("You carefully attach the Vorkath head to the device and create the Torquoise Slayer helm");
                        player.message("Torquoise Slayer helm.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }
    private void combineTwistedKBD(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Recolor the Twisted slayer helmet (i)?", "Yes, sacrifice kbd's head.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(KBD_HEADS, TWISTED_SLAYER_HELMET_I)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(KBD_HEADS), true);
                        player.inventory().remove(new Item(TWISTED_SLAYER_HELMET_I), true);
                        player.inventory().add(new Item(TWISTED_SLAYER_HELMET_I_KBD_HEADS), true);
                        player.message("You carefully attach the kbd's head to the helmet to give it an outstanding look.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineTwistedJad(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Recolor the Twisted slayer helmet (i)?", "Yes, sacrifice 1 million BM.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(new Item(BLOOD_MONEY,1_000_000), new Item(TWISTED_SLAYER_HELMET_I))) {
                            player.message("You did not have 1 million blood money.");
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(BLOOD_MONEY,1_000_000), true);
                        player.inventory().remove(new Item(TWISTED_SLAYER_HELMET_I), true);
                        player.inventory().add(new Item(TWISTED_SLAYER_HELMET_I_JAD), true);
                        player.message("You carefully load helmet with blood money to give it an outstanding look.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineTwistedZuk(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Recolor the Twisted slayer helmet (i)?", "Yes, sacrifice the infernal cape.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(INFERNAL_CAPE, TWISTED_SLAYER_HELMET_I)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(INFERNAL_CAPE), true);
                        player.inventory().remove(new Item(TWISTED_SLAYER_HELMET_I), true);
                        player.inventory().add(new Item(TWISTED_SLAYER_HELMET_I_INFERNAL_CAPE), true);
                        player.message("You carefully attach the infernal cape to the helmet to give it an outstanding look.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineTwistedVamp(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Recolor the Twisted slayer helmet (i)?", "Yes, sacrifice the vampyre dust.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(VAMPYRE_DUST, TWISTED_SLAYER_HELMET_I)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(VAMPYRE_DUST), true);
                        player.inventory().remove(new Item(TWISTED_SLAYER_HELMET_I), true);
                        player.inventory().add(new Item(TWISTED_SLAYER_HELMET_I_VAMP_DUST), true);
                        player.message("You carefully attach the vampyre dust to the helmet to give it an outstanding look.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineTwistedSkot(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Recolor the Twisted slayer helmet (i)?", "Yes, sacrifice Dark claw.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(DARK_CLAW, TWISTED_SLAYER_HELMET_I)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(DARK_CLAW), true);
                        player.inventory().remove(new Item(TWISTED_SLAYER_HELMET_I), true);
                        player.inventory().add(new Item(HYDRA_SLAYER_HELMET_I), true);
                        player.message("You carefully attach Dark claw to the helmet to give it an outstanding look.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineTwistedHydra(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Recolor the Twisted slayer helmet (i)?", "Yes, sacrifice hydra's head.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(ALCHEMICAL_HYDRA_HEADS, TWISTED_SLAYER_HELMET_I)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(ALCHEMICAL_HYDRA_HEADS), true);
                        player.inventory().remove(new Item(TWISTED_SLAYER_HELMET_I), true);
                        player.inventory().add(new Item(HYDRA_SLAYER_HELMET_I), true);
                        player.message("You carefully attach hydra's head to the helmet to give it an outstanding look.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineTwistedBow(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Recolor the Twisted bow?", "Yes, sacrifice the kit.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(TWISTED_BOW, TWISTED_BOW_KIT)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(TWISTED_BOW), true);
                        player.inventory().remove(new Item(TWISTED_BOW_KIT), true);
                        player.inventory().add(new Item(TWISTED_BOW_I), true);
                        player.message("You carefully attach the kit to the bow to give it an outstanding look.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineScytheOfVitur(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Recolor the Scythe of vitur?", "Yes, sacrifice the kit.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(SCYTHE_OF_VITUR, SCYTHE_OF_VITUR_KIT)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(SCYTHE_OF_VITUR), true);
                        player.inventory().remove(new Item(SCYTHE_OF_VITUR_KIT), true);
                        player.inventory().add(new Item(HOLY_SCYTHE_OF_VITUR), true);
                        player.message("You carefully attach the kit to the scythe to give it an outstanding look.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void createDragonPlatebody(Player player) {
        if (!player.inventory().containsAny(DRAGON_METAL_SHARD, DRAGON_METAL_LUMP, DRAGON_CHAINBODY)) {
            player.message("You're missing some of the items to create the dragon platebody.");
        }
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create the Dragon Platebody?", "Yes", "No");
                setPhase(0);
            }

            @Override
            protected void next() {
                if(isPhase(1)) {
                    stop();
                }
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(DRAGON_CHAINBODY, DRAGON_METAL_LUMP, DRAGON_METAL_SHARD)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(DRAGON_CHAINBODY), true);
                        player.inventory().remove(new Item(DRAGON_METAL_LUMP), true);
                        player.inventory().remove(new Item(DRAGON_METAL_SHARD), true);
                        int slot = player.getAttribOr(AttributeKey.ALT_ITEM_SLOT, -1);
                        player.inventory().add(new Item(21892), slot, true);
                        send(DialogueType.ITEM_STATEMENT, 21892, "", "You combine the shard, lump and chainbody to create a Dragon Platebody.");
                        setPhase(1);
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void createDragonKiteshield(Player player) {
        if (!player.inventory().containsAny(DRAGON_METAL_SHARD, DRAGON_METAL_SLICE, DRAGON_SQ_SHIELD)) {
            player.message("You're missing some of the items to create the dragon kiteshield.");
        }
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create the Dragon Kiteshield?", "Yes", "No");
                setPhase(0);
            }

            @Override
            protected void next() {
                if(isPhase(1)) {
                    stop();
                }
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(DRAGON_SQ_SHIELD, DRAGON_METAL_SLICE, DRAGON_METAL_SHARD)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(DRAGON_SQ_SHIELD), true);
                        player.inventory().remove(new Item(DRAGON_METAL_SLICE), true);
                        player.inventory().remove(new Item(DRAGON_METAL_SHARD), true);
                        int slot = player.getAttribOr(AttributeKey.ALT_ITEM_SLOT, -1);
                        player.inventory().add(new Item(21895), slot, true);
                        send(DialogueType.ITEM_STATEMENT, 21895, "", "You combine the shard, slice and square shield to create a Dragon Kiteshield.");
                        setPhase(1);
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineGuardianBoots(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Create Guardian Boots?", "Yes, I'd like to merge the core usedWith the boots.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(BANDOS_BOOTS, BLACK_TOURMALINE_CORE)) {
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(BANDOS_BOOTS), true);
                        player.inventory().remove(new Item(BLACK_TOURMALINE_CORE), true);
                        player.inventory().add(new Item(GUARDIAN_BOOTS), true);
                        player.message("You merge the black tourmaline core usedWith the boots to create a pair of guardian boots.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    private void combineBladeOfSaeldor(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Upgrade the Blade of saeldor?", "Yes, sacrifice the shards.", "No, not right now.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(isPhase(0)) {
                    if(option == 1) {
                        if(!player.inventory().containsAll(new Item(SAELDOR_SHARD,1000), new Item(BLADE_OF_SAELDOR))) {
                            player.message("You did not have enough shards, a thousand is required to upgrade your blade.");
                            stop();
                            return;
                        }
                        player.inventory().remove(new Item(SAELDOR_SHARD,1000), true);
                        player.inventory().remove(new Item(BLADE_OF_SAELDOR), true);
                        player.inventory().add(new Item(BLADE_OF_SAELDOR_8), true);
                        player.message("You carefully attach the shards to the blade to give it an additional boost.");
                        stop();
                    } else if(option == 2) {
                        stop();
                    }
                }
            }
        });
    }
}
