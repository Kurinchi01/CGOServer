package com.Kuri01.Game.Server.Model;

/**
 * Ein unveränderliches Datenobjekt (DTO), das die wichtigsten Informationen
 * eines Benutzerprofils enthält, nachdem es von einem externen Anbieter
 * wie Google validiert wurde.
 *
 * @param googleId Die eindeutige, von Google vergebene ID des Benutzers. Dies ist der primäre Identifikator.
 * @param email Die E-Mail-Adresse des Benutzers.
 * @param name Der Anzeigename des Benutzers.
 */
public record GoogleUserProfile(
        String googleId,
        String email,
        String name
) {
}
