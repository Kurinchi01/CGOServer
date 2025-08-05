# CardGameOne - Backend

Willkommen zum Backend-Repository für **CardGameOne**, ein kartenbasiertes RPG. Dieses Projekt wird mit **Java**, dem **Spring Boot Framework** und **JPA/Hibernate** für die Datenbank-Interaktion entwickelt.

---

## 🚀 Features

* **Spieler-Management:** Authentifizierung, Speicherung von Charakterdaten, Rollen und Währungen.
* **Item-System:** Ein flexibles System für Ausrüstung, Verbrauchsgegenstände und Loot-Truhen mit Stats und Eigenschaften.
* **Inventar & Ausrüstung:** Persistente Verwaltung von Spieler-Inventaren und angelegter Ausrüstung.
* **Spiel-Logik:** Serverseitige Logik zur Verwaltung von Spielrunden, Monstern und Kapiteln.
* **Sicherheit:** JWT-basiertes Authentifizierungssystem zum Schutz der API-Endpunkte.

---

## 🏛️ Architektur: Datenbankstruktur

Das Herzstück des Backends ist eine relationale Datenbank, die den gesamten Spielzustand verwaltet. Die Struktur ist in drei Hauptbereiche unterteilt: **Charaktere**, **Items** und die **Slots**, die beide miteinander verbinden.

### Charakter-Schema

Spieler und Monster werden über eine `SINGLE_TABLE`-Vererbungsstrategie in einer zentralen `CHARACTER`-Tabelle verwaltet. Dies ermöglicht eine einfache Referenzierung von beliebigen Charakteren im Spiel.

```mermaid
classDiagram
    Character <|-- Player
    Character <|-- Monster

    class Character {
        +LONG id (PK)
        +String character_type
        +String name
        +float max_hp
    }

    class Player {
        +String google_id
        +int level
        +Equipment equipment (1-1)
        +Inventory inventory (1-1)
        +PlayerWallet wallet (1-1)
        +Set~String~ roles (1-N)
    }

    class Monster {
        +String rarity
    }
    
    Player "1" -- "1" PlayerWallet
    Player "1" -- "1..*" PlayerRole
    Player "1" -- "1..*" CurrencyTransaction
```
* **CHARACTER:** Die Basistabelle für alle lebenden Entitäten.
* **PLAYER_ROLES:** Speichert die Berechtigungen eines Spielers (z.B. ROLE_USER, ROLE_ADMIN).
* **PLAYER_WALLET:** Verwaltet die Spiel- und Echtgeld-Währungen eines Spielers.
* **CURRENCY_TRANSACTION:** Ein detailliertes Logbuch jeder Währungstransaktion zur Nachverfolgung.

### Item-Schema

Ähnlich wie bei den Charakteren wird für alle Items eine SINGLE_TABLE-Vererbungsstrategie verwendet. Das macht das System extrem flexibel für zukünftige Item-Typen.

```mermaid
classDiagram
    Item <|-- EquipmentItem
    Item <|-- ConsumableItem
    Item <|-- LootChest

    class Item {
        +LONG id (PK)
        +String item_type
        +String name
        +String rarity
        +String icon_name
    }

    class EquipmentItem {
        +String equipment_slot
        +Map~String, int~ stats (1-N)
    }
    
    class ConsumableItem {
      +String effect
    }

    EquipmentItem "1" -- "1..*" ItemStat
```
* **ITEM:** Die zentrale Tabelle für alle Item-Blaupausen.
* **ITEM_STATS:** Eine separate Tabelle zur Speicherung flexibler Stats für Ausrüstungsgegenstände.

### Slot-Schema (Das Bindeglied)
Um die Items mit den Spielern (über Inventar und Ausrüstung) zu verbinden, wird eine JOINED-Vererbungsstrategie verwendet. Dies sorgt für eine saubere und normalisierte Datenbankstruktur.

```mermaid
classDiagram
    ItemSlot <|-- InventorySlot
    ItemSlot <|-- EquipmentSlot
    
    class ItemSlot {
        +LONG id (PK)
        +Item item (FK)
    }

    class InventorySlot {
      +Inventory inventory (FK)
      +int quantity
      +int slot_index
    }

    class EquipmentSlot {
      +Equipment equipment (FK)
      +String slot_enum
    }

    Inventory "1" -- "1..*" InventorySlot
    Equipment "1" -- "1..*" EquipmentSlot
    ItemSlot "1" -- "0..1" Item
```
* **ITEM_SLOT:** Die Basis-Tabelle, die einen Slot als Konzept definiert.
* **INVENTORY_SLOT:** Eine spezialisierte Tabelle, die einen Platz im Inventar eines Spielers repräsentiert.
* **EQUIPMENT_SLOT:** Eine spezialisierte Tabelle, die einen Ausrüstungsplatz eines Spielers repräsentiert.
---
# Gesamtes InventarSystem

```mermaid
classDiagram
    direction LR
    class Character {
        <<Abstract>>
        +Long id (PK)
        +String name
        +int level
        +int experiencePoints
    }
    class Player {
        +String googleId
        +Set~String~ roles
    }
    Character <|-- Player
    class PlayerWallet {
        +Long id (PK, FK)
        +long gold
        +long candy
    }
    class Equipment {
        +Long id (PK)
        +Map~EquipmentSlotEnum, EquipmentSlot~ slots
    }
    class Inventory {
        +Long id (PK)
        +int capacity
        +List~InventorySlot~ slots
    }
    Player "1" -- "1" PlayerWallet : has
    Player "1" -- "1" Equipment : has
    Player "1" -- "1" Inventory : has
    class ItemSlot {
        <<Abstract>>
        +Long id (PK)
        +Item item
    }
    class EquipmentSlot {
        +EquipmentSlotEnum slotEnum
    }
    class InventorySlot {
        +int slotIndex
        +int quantity
    }
    ItemSlot <|-- EquipmentSlot
    ItemSlot <|-- InventorySlot
    class Item {
        <<Abstract>>
        +Long id (PK)
        +String name
        +String description
        +Rarity rarity
        +String iconName
    }
    class EquipmentItem {
         +EquipmentSlotEnum equipmentSlotEnum
         +Map~String, Integer~ stats
    }
    Item <|-- EquipmentItem
    Equipment "1" -- "0..*" EquipmentSlot : contains
    Inventory "1" -- "0..*" InventorySlot : contains
    ItemSlot "1" -- "0..1" Item : holds
```
