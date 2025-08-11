package com.Kuri01.Game.Server.Service.Card;


import com.Kuri01.Game.Server.DTO.Card.SolverResult;
import com.Kuri01.Game.Server.Model.Cards.BoardGenerator;
import com.Kuri01.Game.Server.Model.Cards.BoardStatus;
import com.Kuri01.Game.Server.DTO.Card.GeneratedBoard;
import com.Kuri01.Game.Server.Model.Cards.PreCalculatedBoard;
import com.Kuri01.Game.Server.Repository.Card.PreCalculatedBoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BoardGenerationService {

    private final PreCalculatedBoardRepository boardRepository;
    private final BoardSolverService boardSolver; // Der (noch zu erstellende) Solver
    private final BoardGenerator boardGenerator; // Der (noch zu erstellende) Generator

    @Autowired
    public BoardGenerationService(PreCalculatedBoardRepository boardRepository, BoardSolverService boardSolver, BoardGenerator boardGenerator) {
        this.boardRepository = boardRepository;
        this.boardSolver = boardSolver;
        this.boardGenerator = boardGenerator;
    }

    /**
     * Diese Methode wird automatisch von Spring ausgeführt.
     * fixedRate = 3600000 -> Führe diese Methode alle 60 Minuten aus.
     * initialDelay = 60000 -> Warte 1 Minute nach dem Server-Start, bevor sie das erste Mal läuft.
     */
    @Scheduled(initialDelay = 20000, fixedRate = 3600000)
    public void generateAndAnalyzeBoards() {
        log.info("Starte geplanten Task: Generiere und analysiere neue Spielfelder...");

        long availableBoards = boardRepository.countByStatus(BoardStatus.AVAILABLE);
        final long TARGET_POOL_SIZE = 5000; // Ziel: immer 5000 verfügbare Boards haben

        if (availableBoards >= TARGET_POOL_SIZE) {
            log.info("Board-Pool ist ausreichend gefüllt ({} verfügbare Boards). Überspringe Generierung.", availableBoards);
            return;
        }

        int boardsToGenerate = (int) (TARGET_POOL_SIZE - availableBoards);
        log.info("Generiere {} neue Spielfelder, um den Pool aufzufüllen.", boardsToGenerate);
        int generatedCount = 0;

        for (int i = 0; i < boardsToGenerate * 2; i++) { // Wir generieren mehr, da nicht alle lösbar sein werden
            if (generatedCount >= boardsToGenerate) break;

            // 1. Generiere ein zufälliges Spielfeld
            GeneratedBoard board = boardGenerator.generateRandomBoard();

            // 2. Analysiere es mit dem Solver
            SolverResult analysis = boardSolver.analyze(board);

            // 3. Wenn es lösbar ist, speichere es in der Datenbank
            if (analysis.isSolvable()) {
//                PreCalculatedBoard preCalculatedBoard = new PreCalculatedBoard();
//                preCalculatedBoard.setBoardLayoutJson(board.getLayoutAsJson());
//                preCalculatedBoard.setStockPileJson(board.getStockAsJson());
//                preCalculatedBoard.setSolvable(true);
//                preCalculatedBoard.setDifficultyScore(analysis.getDifficulty());
//                preCalculatedBoard.setLongestCombo(analysis.getLongestCombo());

//                boardRepository.save(preCalculatedBoard);
                generatedCount++;
            }
        }
        log.info("Task beendet. {} neue, lösbare Spielfelder zum Pool hinzugefügt.", generatedCount);
    }
}
