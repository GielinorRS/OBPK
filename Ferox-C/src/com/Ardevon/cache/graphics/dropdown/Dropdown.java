package com.Ardevon.cache.graphics.dropdown;

import com.Ardevon.Client;
import com.Ardevon.cache.graphics.widget.Widget;
import com.Ardevon.model.content.Keybinding;

public enum Dropdown {

    KEYBIND_SELECTION() {
        @Override
        public void selectOption(int selected, Widget dropdown) {
            Keybinding.bind((dropdown.id - Keybinding.MIN_FRAME) / 3, selected);
        }
    },

    PLAYER_ATTACK_OPTION_PRIORITY() {
        @Override
        public void selectOption(int selected, Widget r) {
            Client.singleton.setting.player_attack_priority = selected;
        }
    },

    NPC_ATTACK_OPTION_PRIORITY() {
        @Override
        public void selectOption(int selected, Widget r) {
            Client.singleton.setting.npc_attack_priority = selected;
        }
    };

    private Dropdown() {
    }

    public abstract void selectOption(int selected, Widget r);
}
