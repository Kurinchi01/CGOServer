package com.Kuri01.Game.Server.Repository.Card;


import com.Kuri01.Game.Server.Model.Cards.BoardStatus;
import com.Kuri01.Game.Server.Model.Cards.PreCalculatedBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreCalculatedBoardRepository extends JpaRepository<PreCalculatedBoard, Long> {
    // Zählt, wie viele verfügbare Boards es gibt
    long countByStatus(BoardStatus status);

    // Findet ein zufälliges, verfügbares Board (Beispiel für eine benutzerdefinierte Abfrage)
    // Die genaue Syntax für "zufällig" kann je nach finalen Datenbank (H2, PostgreSQL) variieren.
    @Query(value = "SELECT * FROM pre_calculated_board WHERE status = 'AVAILABLE' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<PreCalculatedBoard> findRandomAvailableBoard();
}