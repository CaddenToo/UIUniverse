package arnett.uIUniverse.ui.inventory.chestInvetory.editor;

import arnett.uIUniverse.UIUniverse;
import arnett.uIUniverse.ui.inventory.slotTypes.BaseSlot;
import arnett.uIUniverse.ui.inventory.slotTypes.DisplaySlot;
import arnett.uIUniverse.ui.inventory.slotTypes.SlotManager;
import arnett.uIUniverse.ui.inventory.slotTypes.StorageSlot;
import arnett.uIUniverse.ui.inventory.slotTypes.buttons.editing.SaveEditButton;
import arnett.uIUniverse.ui.inventory.chestInvetory.ChestUIHolder;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ChestUIEditor extends ChestUIHolder {

    /**
     * Used to save the yaml of the original item slot to an Item stack representation
     */
    public static NamespacedKey slotRepresentation = new NamespacedKey(UIUniverse.singleton, "slotrep");

    /**
     * Used to save the original character key used by the slot
     */
    public static NamespacedKey slotCharacterKey = new NamespacedKey(UIUniverse.singleton, "slotkey");

    /**
     * Used to save the class type of the slot (ex// DisplaySlot, ButtonSlot, ect.)
     */
    public static NamespacedKey slotTypeKey = new NamespacedKey(UIUniverse.singleton, "slottype");

    /**
     * the original menu this editor is base off of
     */
    ChestUIHolder baseMenu;

    //define the current edit layout
    int cutInRows = 0;
    boolean reverseEditor = false;

    public HashMap<Character, BaseSlot> slotRepresentations = new HashMap<>();


    /**
     * Sets up the editor menu by copying the layout and filling the slotRepresentations with the original slots
     * and the Definitions with moveable storage slots containing item stacks of the slots they represent
     * @param menu
     */
    public ChestUIEditor(ChestUIHolder menu) {

        //call a constructor because I guess this is required?
        super(menu);

        /*
        anyway on to the actual logic, basically the idea here is that
        we are copying the menu provided but converting the slots to all storage so it's movable
         */

        //save the base
        baseMenu = menu;

        //copy over the layout the base menu has
        layout = menu.layout;

        //we convert all the base menu slots as storage since we really only need them to be movable
        menu.definitions.forEach((character, baseSlot) -> {
            //set it as one of the slots so it can be moved around
            definitions.put(character, convertToSlotRepresentation(baseSlot, character));

            //store the actual base slots in a separate map used when saving the menu
            slotRepresentations.put(character, baseSlot);
        });

        //layout and definitions of the base menu have been converted!
        //all the editor tools are saved only to be added to display when opened

    }


    /**
     * Defined the slots used for the editor section of the UI
     */
    protected BaseSlot[][] getEditorSlots()
    {
        return new BaseSlot[][]{
                {
                        new DisplaySlot(Material.GLOW_ITEM_FRAME, 1),
                        new DisplaySlot(Material.DIAMOND, 1),
                        null,
                        null,
                        new DisplaySlot(Material.DIRT, 1),
                        new DisplaySlot(Material.AXOLOTL_SPAWN_EGG, 1),
                        new DisplaySlot(Material.LAVA_BUCKET, 1),
                        null,
                        new SaveEditButton(Material.GREEN_BANNER, 1),
                }
        };
    }

    @Override
    public BaseSlot getDefaultSlot() {
        return new StorageSlot(ItemStack.empty());
    }



    @Override
    public String[] getDefaultLayout() {
        return new String[0];
    }



    @Override
    public Map<Character, BaseSlot> getDefaultDefinitions() {
        return Map.of();
    }



    @Override
    public NamespacedKey getIdentifier() {
        return baseMenu.getIdentifier();
    }



    /**
     * overrides inventory building so that it adds on the editor slots here instead of overwriting the layout
     * because the lines may cut in
     */
    @Override
    public Inventory buildInventory() {
        int requestedRows = layout.length;
        BaseSlot[][] editorSlots = getEditorSlots();
        int editorRows = editorSlots.length;
        int requiredRows = requestedRows + editorRows;

        //the amount of rows we need to cut in to display the editing tools
        cutInRows = Math.max(0, requiredRows - 6);
        int definedRows = requestedRows - cutInRows;

        //create the inventory (if we don't need to cut in we need extra rows)
        Inventory inv = Bukkit.createInventory(this, requiredRows * 9, MiniMessage.miniMessage().deserialize(""));


        //set content in inventory for the defined rows
        for (int i = 0; i < definedRows; i++)
        {
            int k = 0;
            int offset = 9 * i;

            String line = layout[i];

            for (k = 0; k < line.length(); k++)
            {
                //we are within what was defined for this line, so get the defined slot's content
                inv.setItem(k + offset, definitions.getOrDefault(line.charAt(k), getDefaultSlot()).getContent());
            }

            //we are over what was defined for this line, so the rest gets filled with default slots
            for (; k < 9; k++)
            {
                inv.setItem(k + offset, definitions.get(' ').getContent());
            }
        }

        for (int i = 0; i < editorRows; i++)
        {
            int k;
            int offset = 9 * (i + definedRows);
            BaseSlot[] line = editorSlots[i];

            for (k = 0; k < line.length; k++)
            {
                //we are within what was defined for this line, so get the defined slot's content
                inv.setItem(k + offset, line[k] == null ? ItemStack.empty() : line[k].getContent());
            }

            //we are over what was defined for this line, so the rest gets filled with air
            for (; k < 9; k++)
            {
                inv.setItem(k + offset, ItemStack.empty());
            }
        }

        //return the now filled inventory
        return inv;
    }


    /**
     * gets the base slot at the specified index, overridden to include logic to check if it is an editor slot
     * @throws IndexOutOfBoundsException
     */
    @Override
    public BaseSlot getBaseSlot(int slotNumber) throws IndexOutOfBoundsException {

        int clickedRow = slotNumber/9 + 1;
        int clickedSlotInRow = slotNumber%9;

        //we are clicking outside the predefined layout area onto an editor slot
        if(clickedRow > (layout.length - cutInRows))
        {
            BaseSlot[] line = getEditorSlots()[slotNumber/9 - layout.length];

            if(line.length > clickedSlotInRow)
            {
                return line[clickedSlotInRow];
            }
            else
            {
                return new DisplaySlot(ItemStack.empty());
            }

        }

        return definitions.get(layout[slotNumber / 9].charAt(slotNumber % 9));
    }


    /**
     * Writes the edited menu (not the editor menu but the rearranged base menu) to yaml
     * @return
     * @throws MatchException
     */
    @Override
    public YamlConfiguration writeToYaml() throws MatchException {
        YamlConfiguration yaml = new YamlConfiguration();

        StringBuilder rootKey = new StringBuilder();
        int rootKeyLength = rootKey.length();

        //track the used characters so we don't store excess definitions
        HashSet<Character> usedKeys = new HashSet<>();

        //write the layout (this part is the same)
        for (int i = 0; i < layout.length; i++) {

            //append this line's key
            rootKey.append("line-").append(i+1).append('.');
            int lineKeyLength = rootKey.length();

            //process the characters in the line
            for (int k = 0; k < layout[i].length(); k++) {

                char key = layout[i].charAt(k);

                //set the slot's yaml
                yaml.set(rootKey.append(k).toString(), Character.toString(key));

                //cut off that slot key
                rootKey.setLength(lineKeyLength);
                usedKeys.add(key);

            }

            //cut off the line key
            rootKey.setLength(rootKeyLength);
        }

        //copy over default definitions
        rootKey.append("definitions.");
        int definitionsKeyLength = rootKey.length();

        //save the default slot
        {
            //remove this key so we don't process it again in the other definitions
            usedKeys.remove(' ');

            //attach the slots yaml file to this section
            yaml.set(rootKey.append(' ').toString(), baseMenu.getDefaultSlot().writeToYaml().getValues(false));

            //cut off this character's key
            rootKey.setLength(definitionsKeyLength);
        }

        //write the default definitions
        baseMenu.getDefaultDefinitions().forEach((character, baseSlot) ->
        {
            //remove this key so we don't process it again in the other definitions
            usedKeys.remove(character);

            System.out.println("saving " + character);

            //attach the slots yaml file to this section
            yaml.set(rootKey.append(character).toString(), baseSlot.writeToYaml().getValues(false));

            //cut off this character's key
            rootKey.setLength(definitionsKeyLength);
        });

        //write the representations
        slotRepresentations.forEach((character, baseSlot) ->
        {
            //don't add this if it is not used
            if(!usedKeys.contains(character))
            {
                return;
            }

            System.out.println("saving Rep " + character);

            //attach the slots yaml file to this section
            yaml.set(rootKey.append(character).toString(), baseSlot.writeToYaml().getValues(false));

            //cut off this character's key
            rootKey.setLength(definitionsKeyLength);
        });


        return yaml;
    }


    /**
     * Applies changes to the layout and definitions (useful for when cutting in and moving editor rows)
     */
    public void saveWork()
    {
        Inventory inventory = getInventory();
        ItemStack[] content = inventory.getContents();
        int invSize = inventory.getSize();
        int targetRows = layout.length - cutInRows;

        for (int i = 0; i < invSize; i++) {
            int row = i / 9 + 1;

            if(reverseEditor && row < cutInRows)
            {
                //skip this because we are in an editor row
                continue;
            }
            else if(row > targetRows)
            {
                //skip this because we are in an editor row
                continue;
            }

            //save the slot representation to the corresponding slot

            //first register it
            char key = registerSlotRepresentation(content[i]);

            //now save it to the layout
            StringBuilder line = new StringBuilder(layout[row-1]);
            line.setCharAt(i%9, key);
            layout[row-1] = line.toString();
        }

        //clear out the unused definitions
        cleanDefinitions();
    }


    /**
     * Clears the Definitions list to make room for new ones when saving
     */
    public void cleanDefinitions()
    {
        HashSet<Character> used = new HashSet<>();

        for (String line : layout)
        {
            for(char c : line.toCharArray())
            {
                used.add(c);
            }
        }

        //also keep all the default slots
        used.addAll(baseMenu.getDefaultDefinitions().keySet());
        used.add(' ');

        definitions.entrySet().removeIf((entry) ->
        {
            //if it is not used then remove it
            return !used.contains(entry.getKey());
        });


        slotRepresentations.entrySet().removeIf((entry) ->
        {
            //if it is not used then remove it
            return !used.contains(entry.getKey());
        });
    }


    /**
     * Registers an ItemStack in the definitions for when saving layouts & definitions
     */
    public char registerSlotRepresentation(ItemStack stack) {

        if(stack == null || stack.isEmpty())
        {
            return ' ';
        }

        ItemMeta meta = stack.getItemMeta();

        // if this is a slot representation it may already be defined
        // and if not we should just copy over the existing definition
        if(isSlotRepresentation(stack))
        {
            Character slotKey = getIdenticalSlotKey(stack);

            //only create a new definition if this is not already defined
            if(slotKey == null)
            {
                slotKey = getAvailableCharacter();

                try {


                    YamlConfiguration slotYaml = new YamlConfiguration();

                    slotYaml.loadFromString(
                            meta.getPersistentDataContainer().get(
                                    slotRepresentation,
                                    PersistentDataType.STRING
                            )
                    );
                    NamespacedKey typeKey = NamespacedKey.fromString(meta.getPersistentDataContainer().get(
                            slotTypeKey,
                            PersistentDataType.STRING
                    ));

                    BaseSlot readSlot = SlotManager.getSlotClass(typeKey)
                            .getConstructor(ConfigurationSection.class).newInstance(slotYaml);

                    //update the stack size since that's the only things that would reasonably change
                    readSlot.getContent().setAmount(stack.getAmount());

                    definitions.put(slotKey, convertToSlotRepresentation(readSlot, slotKey));

                    slotRepresentations.put(slotKey, readSlot);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            return slotKey;

        }
        else
        {
            char key = getItemKey(stack);

            //if it is already defined just return this
            if(definitions.containsKey(key))
            {
                return key;
            }
            else
            {
                //it is not defined so define it first before returning it
                UIUniverse.logger.warning("Passed Item slot does NOT contain the information to be converted to ItemSlot");

                BaseSlot defaultSlot = baseMenu.getDefaultSlot();

                defaultSlot.setContent(cloneWithoutPdc(stack));

                slotRepresentations.put(key, defaultSlot);

                //add it in definitions so it gets skipped for later regenerations
                definitions.put(key, convertToSlotRepresentation(defaultSlot, key));

                return key;
            }
        }
    }


    /**
     * Converts a normal menu slot ot an editor representation slot (so it can be moved around)
     * @param baseSlot slot to convert
     * @param key new key to give this representation
     * @return Converted Editor ready Storage slot
     */
    public BaseSlot convertToSlotRepresentation(BaseSlot baseSlot, char key)
    {

        ItemStack original = baseSlot.getContent();

        //get a visual copy using the material and stack size

        ItemStack convertedSlotItem;

        if(original == null || original.isEmpty())
        {
            convertedSlotItem = ItemStack.of(Material.GRAY_STAINED_GLASS_PANE, 1);
        }
        else
        {
            convertedSlotItem = cloneWithoutPdc(original);
        }

        ItemMeta slotMeta = convertedSlotItem.getItemMeta();

        slotMeta.displayName(
                Component.text(
                        baseSlot.getIdentifier() + " - " + key,
                        TextColor.color(DyeColor.RED.getColor().asRGB()),
                        TextDecoration.BOLD
                )
        );

        slotMeta.lore(baseSlot.getDisplayLore());

        //clear any editor tags off the base slot content to prevent data overflow


        // tag this as a slot representation so that we can stop these from getting into player circulation if,
        // for example, they were to be thrown out of the inventory
        slotMeta.getPersistentDataContainer().set(
                slotRepresentation,
                PersistentDataType.STRING,
                baseSlot.writeToYaml().saveToString()
        );
        slotMeta.getPersistentDataContainer().set(
                slotCharacterKey,
                PersistentDataType.STRING,
                Character.toString(key)
        );
        slotMeta.getPersistentDataContainer().set(
                slotTypeKey,
                PersistentDataType.STRING,
                baseSlot.getIdentifier().toString()
        );

        convertedSlotItem.setItemMeta(slotMeta);

        return new StorageSlot(convertedSlotItem);
    }

    /**
     * Creates the display ItemStack clear of all pdc data from the passed one as to
     * not collide with other plugin pdc checks
     */
    public ItemStack cloneWithoutPdc(ItemStack stack)
    {
        ItemStack displayClone = stack.clone();

        ItemMeta cloneMeta = displayClone.getItemMeta();

        PersistentDataContainer clonePdc = cloneMeta.getPersistentDataContainer();

        if(!clonePdc.isEmpty())
            clonePdc.getKeys().forEach(clonePdc::remove);

        displayClone.setItemMeta(cloneMeta);

        return displayClone;
    }

    /**
     * @return A character not used by any slot in this menu
     */
    @Override
    protected char getAvailableCharacter() {
        for(char i = 'A'; i < 'Z'; i++)
        {
            if(!slotRepresentations.containsKey(i))
            {
                return i;
            }
        }
        for(char i = 'a'; i < 'z'; i++)
        {
            if(!slotRepresentations.containsKey(i))
            {
                return i;
            }
        }
        for(char i = '0'; i < '9'; i++)
        {
            if(!slotRepresentations.containsKey(i))
            {
                return i;
            }
        }
        for(char i = '!'; i < 'ÿ'; i++)
        {
            if(!slotRepresentations.containsKey(i))
            {
                return i;
            }
        }
        return '0';
    }

    /**
     * Opens the menu editor for a player
     */
    public void openEditor(Player player)
    {
        player.openInventory(getInventory());
    }

    /**
     * Checks against every existing definition entry to see if this has already been defined
     * @param stack Stack to check
     * @return true if match was found
     */
    public Character getIdenticalSlotKey(ItemStack stack)
    {
        for (var slot : definitions.entrySet())
        {
            if(slot.getValue().getContent().equals(stack))
            {
                return slot.getKey();
            }
        }

        return null;
    }

    /**
     * @param stack ItemStack to check
     * @return Is this stack a slot Representation
     */
    public static boolean isSlotRepresentation(ItemStack stack)
    {
        if(stack == null)
            return false;

        PersistentDataContainerView pdc = stack.getPersistentDataContainer();
        return (pdc.has(slotRepresentation) && pdc.has(slotCharacterKey));
    }

    /**
     * Removes all slot representaitions when closing the menu
     */
    @Override
    public void onMenuClose(InventoryCloseEvent e) {
        PlayerInventory playerInventory = e.getPlayer().getInventory();
        ItemStack[] content = playerInventory.getContents();

        for(int i = 0; i < content.length; i++)
        {
            if(isSlotRepresentation(content[i]))
            {
                //clear this item
                playerInventory.setItem(i, ItemStack.empty());
            }
        }
    }


}
