=============================================================Datenbank Struktur=============================================================
+--------------------------------+
|         CHARACTER              |  <-- Haupttabelle für Spieler & Monster
|--------------------------------|
| (PK) id: LONG                  |
|      character_type: VARCHAR   |  <-- Unterscheidet 'PLAYER' von 'MONSTER'
|      name: VARCHAR              |
|      max_hp: FLOAT              |
|      attack: FLOAT              |
|      charge_rate: FLOAT         |
|      google_id: VARCHAR (nur Player) |
|      level: INTEGER (nur Player)     |
|      rarity: VARCHAR (nur Monster)  |
| (FK) equipment_id: LONG        | ------+
| (FK) inventory_id: LONG        | ------|------+
| (FK) wallet_id: LONG           | ------|------|------+
+--------------------------------+       |      |      |
        |      ^                           |      |      |
        |      | (1-zu-N, @ElementCollection) |      |      |
        |      |                           V      V      V
+---------------------+            +---------------+ +-------------+ +----------------+
|    PLAYER_ROLES     |            |   EQUIPMENT   | |  INVENTORY  | |  PLAYER_WALLET |
|---------------------|            |---------------| |-------------| |----------------|
| (FK) player_id: LONG|            | (PK) id: LONG | | (PK) id: LONG | | (PK,FK) id: LONG |
|      role: VARCHAR  |            +---------------+ | capacity: INT | | gold, gems: LONG |
+---------------------+                  |           |             +-+----------------+
        |                                | (1-zu-N)  | (1-zu-N)      |
        | (1-zu-N)                       |           |               | (1-zu-1, Player)
        V                                |           V               V
+--------------------------+             |  [EQUIPMENT_SLOT]  [INVENTORY_SLOT]
|  CURRENCY_TRANSACTION    |             |  (Details siehe unten)
|--------------------------|             |
| (PK) id: LONG            |             |
| (FK) player_id: LONG     |             |
|      amount_changed: LONG|             |
|      reason: VARCHAR     |             |
|      timestamp: DATETIME |             |
+--------------------------+             |
                                         | (1-zu-1, Player)
                                         +----------------------------> (1-zu-1, Player)


============================================================================================================================================

+------------------------------------+
|                ITEM                |
|------------------------------------|
| (PK) id: LONG                      |
|      item_type: VARCHAR            | <-- Unterscheidet 'EQUIPMENT', 'CHEST' etc.
|      name: VARCHAR                  |
|      description: VARCHAR           |
|      rarity: VARCHAR                |
|      icon_name: VARCHAR             |
|      equipment_slot: VARCHAR (nur EquipmentItem) |
|      effect: VARCHAR (nur ConsumableItem)      |
+------------------------------------+
       |
       | (1-zu-N, @ElementCollection)
       V
+--------------------------+
|        ITEM_STATS        |
|--------------------------|
| (FK) item_id: LONG       |
|      stat_name: VARCHAR  |
|      stat_value: INTEGER |
+--------------------------+

============================================================================================================================================

                            +--------------------------+
                            |        ITEM_SLOT         | <-- Basis-Tabelle
                            |--------------------------|
                            | (PK) id: LONG            |
                            | (FK) item_id: LONG       | --+
                            +--------------------------+   |
                               |          ^                |
      (Vererbung, 1-zu-1)      |          | (Vererbung, 1-zu-1)  | (Beziehung zu Item)
                               V          |                V
+------------------------------------+    |        +------------------------------+
|          INVENTORY_SLOT            |    |        |        EQUIPMENT_SLOT          |
|------------------------------------|    |        |------------------------------|
| (PK,FK) id: LONG                   |    |        | (PK,FK) id: LONG             |
| (FK) inventory_id: LONG            | ---+        | (FK) equipment_id: LONG        |
|         quantity: INTEGER          |             |       slot_enum: VARCHAR       |
|         slot_index: INTEGER        |             +------------------------------+
+------------------------------------+
