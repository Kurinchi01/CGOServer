package com.Kuri01.Game.Server.Controller;

import com.Kuri01.Game.Server.Auth.GoogleLoginRequest;
import com.Kuri01.Game.Server.DTO.GoogleUserProfile;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.PlayerRepository;
import com.Kuri01.Game.Server.Service.GoogleTokenValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.mockito.Mockito.*;


/**
 * Integrationstests für den AuthController.
 */
@SpringBootTest
@AutoConfigureMockMvc // Richtet MockMvc für uns ein, um HTTP-Anfragen zu simulieren.
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc; // Unser Werkzeug zum Senden von Test-Anfragen.


    @Autowired
    private ObjectMapper objectMapper; // Wandelt Java-Objekte in JSON-Strings um.

    @MockitoBean // Ersetzt den echten GoogleTokenValidator durch einen Mock.
    private GoogleTokenValidator googleTokenValidator;

    @MockitoBean // Ersetzt auch das PlayerRepository durch einen Mock für volle Kontrolle.
    private PlayerRepository playerRepository;

    // Wir brauchen keinen JwtService zu mocken, da wir seine echte Funktionalität testen wollen.

    @Test
    void login_whenNewUserWithValidToken_shouldCreatePlayerAndReturnJwt() throws Exception {
        // ========== Vorbereiten ==========
        String validToken = "gueltiges-token-fuer-neuen-user";
        String googleId = "google-id-12345";
        GoogleUserProfile mockProfile = new GoogleUserProfile(googleId, "neu@example.com", "Neuer Spieler");

        // 1. Simuliere eine erfolgreiche Google-Validierung.
        when(googleTokenValidator.validate(validToken)).thenReturn(Optional.of(mockProfile));

        // 2. Simuliere, dass der Spieler mit dieser Google ID noch NICHT in der DB existiert.
        when(playerRepository.findByGoogleId(googleId)).thenReturn(Optional.empty());

        // 3. Simuliere das Speichern des neuen Spielers.
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GoogleLoginRequest loginRequest = new GoogleLoginRequest(validToken);

        // ========== Ausführen ==========
        ResultActions result = mockMvc.perform(post("/api/auth/login/google")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // ========== Überprüfen ==========
        result.andExpect(status().isOk()); // Wir erwarten HTTP 200 OK
        result.andExpect(jsonPath("$.jwtToken").isNotEmpty()); // Prüfe, ob ein JWT zurückkam.

        // Überprüfe, ob die save-Methode genau einmal aufgerufen wurde.
        // Das ist der Beweis, dass ein neuer Spieler erstellt wurde.
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void login_whenExistingUserWithValidToken_shouldReturnJwt() throws Exception {
        // ========== Vorbereiten ==========
        String validToken = "gueltiges-token-fuer-bekannten-user";
        String googleId = "google-id-67890";
        GoogleUserProfile mockProfile = new GoogleUserProfile(googleId, "alt@example.com", "Bekannter Spieler");
        Player existingPlayer = new Player();
        existingPlayer.setGoogleId(googleId);
        existingPlayer.setName("Bekannter Spieler");

        // 1. Simuliere eine erfolgreiche Google-Validierung.
        when(googleTokenValidator.validate(validToken)).thenReturn(Optional.of(mockProfile));

        // 2. Simuliere, dass der Spieler bereits in der DB GEFUNDEN wird.
        when(playerRepository.findByGoogleId(googleId)).thenReturn(Optional.of(existingPlayer));

        GoogleLoginRequest loginRequest = new GoogleLoginRequest(validToken);

        //========== Ausführen ==========
        ResultActions result = mockMvc.perform(post("/api/auth/login/google")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // ========== Überprüfen ==========
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.jwtToken").isNotEmpty());

        // Überprüfe, dass die save-Methode NIE aufgerufen wurde.
        // Das ist der Beweis, dass kein neuer Spieler erstellt wurde.
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void login_whenTokenIsInvalid_shouldReturnUnauthorized() throws Exception {
        // ========== Vorbereiten ==========
        String invalidToken = "ungueltiges-token";

        // Simuliere, dass die Google-Validierung fehlschlägt (gibt ein leeres Optional zurück).
        when(googleTokenValidator.validate(invalidToken)).thenReturn(Optional.empty());

        GoogleLoginRequest loginRequest = new GoogleLoginRequest(invalidToken);

        // ========== Ausführen ==========
        ResultActions result = mockMvc.perform(post("/api/auth/login/google")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // ========== Überprüfen ==========
        // Erwarte den HTTP-Status 401 Unauthorized.
        result.andExpect(status().isUnauthorized());
    }
}