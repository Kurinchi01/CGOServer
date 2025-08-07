package com.Kuri01.Game.Server.Repository;

import com.Kuri01.Game.Server.Model.RPG.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}