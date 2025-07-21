-- ====================================================================================
--  Items erstellen (Alle Sub-Klassen von 'Item' werden in die 'item'-Tabelle eingefügt)
-- ====================================================================================

-- Hier fügen wir 'EquipmentItem'-Instanzen ein.
-- Beachte die Spalten 'item_type' und 'equipment_slot', die nur für diese Sub-Klasse relevant sind.
INSERT INTO item (id, name, description, rarity, item_type, equipment_slot,quantity,icon_name) VALUES
(1, 'Wood Sword', 'Ein einfacher Anfang.', 'common', 'EQUIPMENT', 'WEAPON',1,'WoodSword'),
(2, 'Simple Helmet', 'Besser als nichts.', 'common', 'EQUIPMENT', 'HELMET',1,'BasicHelmet'),
(3, 'Necklace', 'Knistert vor Energie.', 'common', 'EQUIPMENT', 'NECKLACE',1,'BasicNecklace'),
(4, 'Ring', 'Groß und brutal.', 'common', 'EQUIPMENT', 'RING',1,'BasicRing'),
(5, 'Armor', 'Groß und brutal.', 'common', 'EQUIPMENT', 'ARMOR',1,'BasicArmor'),
(6, 'Shoe', 'Groß und brutal.', 'common', 'EQUIPMENT', 'SHOES',1,'BasicShoes');

-- Stats für die EquipmentItems (diese gehören zur 'item_stats'-Tabelle, die mit 'item' verknüpft ist)
INSERT INTO item_stats (item_id, stat_name, stat_value) VALUES (1, 'ATTACK', 5);
INSERT INTO item_stats (item_id, stat_name, stat_value) VALUES (2, 'DEFENSE', 3);
INSERT INTO item_stats (item_id, stat_name, stat_value) VALUES (3, 'MAGIC_POWER', 12);
INSERT INTO item_stats (item_id, stat_name, stat_value) VALUES (4, 'ATTACK', 25);
INSERT INTO item_stats (item_id, stat_name, stat_value) VALUES (4, 'DEFENSE', -5);


-- Hier fügen wir eine 'ConsumableItem'-Instanz ein.
-- Beachte die Spalten 'effect' und 'effect_value'. Die Spalte 'equipment_slot' wäre hier NULL.
--INSERT INTO item (id, name, description, rarity, item_type, effect, effect_value) VALUES
--(51, 'Kleiner Heiltrank', 'Stellt 50 Lebenspunkte wieder her.', 'common', 'CONSUMABLE', 'HEAL', 50);


-- Hier fügen wir 'LootChest'-Instanzen ein.
-- Diese haben keine der speziellen Spalten von Equipment- oder Consumable-Items. Diese Spalten sind hier NULL.
INSERT INTO item (id, name, description, rarity, item_type,quantity,icon_name) VALUES
(101, 'Gewöhnliche Holztruhe', 'Eine einfache Holztruhe.', 'common', 'CHEST',1,'commonChest'),
(102, 'Robuste Eisentruhe', 'Eine stabilere Truhe mit besserem Inhalt.', 'uncommon', 'CHEST',1,'commonChest');


-- ====================================================================================
--  Charaktere erstellen (Monster und Spieler in der 'character'-Tabelle)
-- ====================================================================================

-- Hier fügen wir 'Monster'-Instanzen in die 'character'-Tabelle ein.
-- Der 'character_type' ist hier 'MONSTER'.
INSERT INTO character (id, name, max_hp, attack, character_type, rarity,charge_rate) VALUES
(201, 'Wald-Goblin', 50, 10, 'MONSTER', 'common',5.0),
(202, 'Höhlen-Orc', 120, 25, 'MONSTER', 'uncommon',5.0),
(203, 'Felsen-Golem', 300, 40, 'MONSTER', 'rare',5.0);

-- Hier fügen wir eine 'Player'-Instanz in die 'character'-Tabelle ein.
-- Der 'character_type' ist 'PLAYER' und es gibt zusätzliche, spieler-spezifische Spalten.
--INSERT INTO equipment (id) VALUES (1);
--INSERT INTO inventory (id) VALUES (1);
--INSERT INTO character (id, name, max_hp, attack, character_type, google_id, level, experience_points, equipment_id, inventory_id) VALUES
--(1, 'Kuri01', 100, 10, 'PLAYER', 'google-id-12345', 1, 0, 1, 1);

--INSERT INTO character (id, name, max_hp, attack, character_type, rarity,charge_rate,google_id) VALUES (1, 'Kuri01', 100, 20, 'PLAYER', null, 5,'Kuri01');

--INSERT INTO player_roles (player_id, role) VALUES (1, 'ROLE_USER');
--INSERT INTO player_roles (player_id, role) VALUES (1, 'ROLE_ADMIN');

-- ====================================================================================
--  Kapitel erstellen und Monster zuweisen
-- ====================================================================================
INSERT INTO chapter (id, name, description, monster_count) VALUES (1, 'Der Pfad des Anfängers', 'Ein guter Ort, um zu starten.', 1);
INSERT INTO chapter (id, name, description, monster_count) VALUES (2, 'Die gefährlichen Höhlen', 'Hier lauern stärkere Gegner.', 2);

-- Verknüpfe die Monster mit den Kapiteln
INSERT INTO chapter_monster_pool (chapter_id, monster_id) VALUES
(1, 201),
(1, 202),
(2, 202),
(2, 203);

-- Erstellt 20 leere Inventar-Slots und weist sie alle dem Inventar mit der ID 1 zu.

