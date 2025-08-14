package com.Kuri01.Game.Server.DTOMapper;

import com.Kuri01.Game.Server.Model.Cards.Card;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Diese Klasse ist ein Mapper für alles, was das TriPeaksCard Game betrifft.
 **/

@Getter
@Setter
@Slf4j
@Component
public class GameMapper {

    private final ObjectMapper objectMapper;

    @Autowired
    public GameMapper() {
        this.objectMapper = new ObjectMapper();
    }


    public List<Card> parseJsonToList(String boardLayoutJson) throws JsonProcessingException {

        // 1. Erstellen Sie eine anonyme TypeReference, die den vollständigen
        //    generischen Typ (List<Card>) für Jackson bewahrt.
        TypeReference<List<Card>> typeRef = new TypeReference<>() {
        };

        // 2. Rufen Sie readValue mit dem JSON-String und der TypeReference auf.
        return objectMapper.readValue(boardLayoutJson, typeRef);

    }
}
