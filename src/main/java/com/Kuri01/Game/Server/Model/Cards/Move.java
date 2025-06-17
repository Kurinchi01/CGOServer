package com.Kuri01.Game.Server.Model.Cards;

/**
 * Repräsentiert einen einzelnen, vollständigen Zug, den ein Spieler ausführt.
 * <p>
 * Diese Klasse ist ein "record", eine spezielle, unveränderliche Datenklasse in Java.
 * Sie dient als Data Transfer Object (DTO) für die Kommunikation zwischen Client und Server.
 *
 * @param action Der Typ der durchgeführten Aktion (z.B. PLAY_CARD oder DRAW_FROM_DECK). Dies ist der wichtigste Teil, um den Zug zu verstehen.
 * @param card   Die Karte, die mit der Aktion verbunden ist. Dieses Feld ist nur relevant für Aktionen wie PLAY_CARD.
 * Es ist {@code null} für Aktionen wie DRAW_FROM_DECK, bei denen keine spezifische Karte vom Spieler ausgewählt wird.
 */
public record Move(
        ActionType action,
        Card card
) {
    // Ein Java 'record' generiert automatisch:
    // - Einen Konstruktor für alle Felder (public Move(ActionType action, Card card))
    // - Getter-Methoden für alle Felder (public ActionType action() und public Card card())
    // - Sinnvolle Implementierungen von equals(), hashCode() und toString()
    // Es ist also nicht nötig, diesen Code manuell zu schreiben.
}