SHANK THE WORLD README:

To Run Game:

If not executing automatically from a jar file, run "MainMenuWindow.java" in the ui package.
For singleplayer, hit the host button followed by the join button. Leave the IP address blank, enter a name and hit ok.
For multiplayer, playing locally requires one person to host and the others to enter the address of the host computer in the IP field.
To play multiplayer online, the host needs to port forward. As in singleplayer, the host always needs to leave the ip blank.

Game Controls:

Q - 	Rotate camera counter-clockwise.
E - 	Rotate camera clockwise.
W - 	Move forward.
A - 	Move left.
S - 	Move backwards.
D -	Move right.
F - 	Interact with objects. (Opening chests & loots corpses).
Space - Attack.	
Tab - 	Toggle Inventory.
Esc - 	Sound options / Close loot menu. Note: Player can't move with loot menu open.
Enter - Toggle Chat.

Inventory Interaction:

-	Hover over an item to view its description.
-	Right-Click weapons/armour to equip/dequip.
-	Right-Click potions to consume them.
-	Drag and drop items to rearrange inventory slots.
-	Drag and drop items between containers and inventory to move them.
-	Drag and drop items off inventory to drop them into the world.

Game Objectives:

To simply kill players and loot the map. To open a chest you need a key and you only start with one. 
Opening a chest breaks the key and if you were unlucky enough to not find one in the chest, 
you will need to get another by killing a player.

To Run Map Editor:

Run "EditorFrame.java" in the mapeditor package. Write either "outside" or "inside" when prompted for location type. (not case sensitive)

Editor Controls:

W - 	Move Camera up.
A -	Move Camera left.
S - 	Move Camera down.
D - 	Move Camera right.

Using the Editor:

- 	To draw, select a terrain, building or entity in the menus on the left and then either click or drag to fill out 		squares.
- 	To view the map how it would appear in the game, switch from editor view to renderer view in the view drop box on the 	left.
- 	To save, hit file, save and then fill in a map name and description. This will then create two files, <mapname>.txt 		and <mapname>-entites.txt.
	These need to be moved into the correct entities and locations folder. From there a txt file connects the doors named 	door.txt and needs to be 
	edited to display the locations in the game.
	

The Map Editor has many problems I didn't have time to fix due to higher priorities and constantly needing to update how it worked.

Creating Entrances On INSIDE Buildings:
	
	Correct:				Incorrect:
	
	FL	FL	FL				FL	FL	FL
	FL	FL	FL	En			FL	FL	En
	FL	FL	FL				FL	FL	FL
	
Creating entrances on outside Buildings is the opposite. Sorry for the confusion.
Also need to make sure the view is North when drawing as well as NOT having a building on the edge / not surrounded by terrain.
