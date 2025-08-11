-- ====================================================================================
--  Items erstellen (Jetzt als eine einzige, flache Tabelle)
-- ====================================================================================

-- Ausrüstbare Items
INSERT INTO item (id, name, description, rarity, icon_name, equipment_slot) VALUES
(1, 'Wood Sword', 'Ein einfacher Anfang.', 'COMMON', 'WoodSword', 'WEAPON'),
(2, 'Simple Helmet', 'Besser als nichts.', 'COMMON', 'BasicHelmet', 'HELMET'),
(3, 'Necklace', 'Knistert vor Energie.', 'COMMON', 'BasicNecklace', 'NECKLACE'),
(4, 'Ring', 'Groß und brutal.', 'COMMON', 'BasicRing', 'RING'),
(5, 'Armor', 'Groß und brutal.', 'COMMON', 'BasicArmor', 'ARMOR'),
(6, 'Shoe', 'Groß und brutal.', 'COMMON', 'BasicShoes', 'SHOES');


-- Stats für die Ausrüstungs-Items
INSERT INTO item_stats (item_id, stat_name, stat_value) VALUES
(1, 'ATTACK', 5),
(2, 'DEFENSE', 3),
(3, 'MAGIC_POWER', 12),
(4, 'ATTACK', 25),
(4, 'DEFENSE', -5);

-- ====================================================================================
--  Charaktere erstellen (Monster)
-- ====================================================================================
INSERT INTO character (id, name, max_hp, attack, character_type, rarity, charge_rate) VALUES
(201, 'Wald-Goblin', 50, 10, 'MONSTER', 'COMMON', 5.0),
(202, 'Höhlen-Orc', 120, 25, 'MONSTER', 'UNCOMMON', 5.0),
(203, 'Felsen-Golem', 300, 40, 'MONSTER', 'RARE', 5.0);

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