package com.japs.backend.backend_BodyFitGym.infrastructure.entry_points.rest.handler;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;
import com.japs.backend.backend_BodyFitGym.application.services.UserService;
import com.japs.backend.backend_BodyFitGym.application.utils.ResponseBuilder;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<User>> save(@Valid @RequestBody User userRequest){

        log.info("[UserController] POST /save - Creando usuario: {}", userRequest.getUserName());

        User user = userService.save(userRequest);
        ApiResponse<User> apiResponse = ResponseBuilder.successMessage("Usuario registrado exitosamente",user);

        log.info("[UserController] POST /save - Usuario creado con ID: {}", user.getId());
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){

        log.info("[UserController] DELETE /delete/{} - Eliminando usuario", id);

        userService.delete(id);
        ApiResponse<Void> apiResponse = ResponseBuilder.successMessage("Usuario eliminado Con Exito");

        log.info("[UserController] DELETE /delete/{} - Usuario eliminado correctamente", id);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse<User>> findById(@PathVariable Long id){

        log.info("[UserController] GET /find-by-id/{} - Buscando usuario", id);

        User user = userService.findById(id);
        ApiResponse<User> apiResponse = ResponseBuilder.successMessage("Usuario Encontrado",user);

        log.info("[UserController] GET /find-by-id/{} - Usuario encontrado: {}", id, user.getUserName());

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-userName/{userName}")
    public ResponseEntity<ApiResponse<User>> findByUserName(@PathVariable String userName){

        log.info("[UserController] GET /find-by-userName/{} - Buscando usuario",userName);

        User user = userService.findByUserName(userName);
        ApiResponse<User> apiResponse = ResponseBuilder.successMessage("Usuario Encontrado",user);

        log.info("[UserController] GET /find-by-username/{} - Usuario encontrado: {}", userName, user.getUserName());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/find-by-document/{document}")
    public ResponseEntity<ApiResponse<User>> findByDocument(@PathVariable String document){

        log.info("[UserController] GET /find-by-document/{} - Buscando usuario",document);

        User user = userService.findByDocument(document);
        ApiResponse<User> apiResponse = ResponseBuilder.successMessage("Usuario Encontrado",user);

        log.info("[UserController] GET /find-by-document/{} - Usuario encontrado: {}", document, user.getUserName());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<User>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String document){

        log.info("[UserController] GET /search - Filtros: name={}, username={}, document={}",name, userName, document);

        UserSearchCriteria userSearchCriteria = new UserSearchCriteria(name,document,userName);

        List<User> users =userService.search(userSearchCriteria);
        ApiResponse<List<User>> apiResponse = ResponseBuilder.successMessage("Usuarios Encontrados",users);

        log.info("[UserController] Get /search - Resultados encontrados: {}",users);

        return ResponseEntity.ok(apiResponse);
    }
}
