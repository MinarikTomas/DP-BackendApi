package sk.stuba.fei.uim.dp.attendanceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.request.CardRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SerialNumberRequest;
import sk.stuba.fei.uim.dp.attendanceapi.response.CardResponse;
import sk.stuba.fei.uim.dp.attendanceapi.service.CardService;

@RestController
@RequestMapping("/card")
@Tag(name = "Card")
public class CardController {

    @Autowired
    private CardService cardService;

    @Operation(
            description = "Create a new card.",
            summary = "Create card."
    )
    @PostMapping
    public ResponseEntity<String> createCard(@RequestBody CardRequest request){
        this.cardService.createCard(request);
        return new ResponseEntity<>("Card created", HttpStatus.CREATED);
    }

    @Operation(
            description = "Returns the card with given ID.",
            summary = "Get card using ID"
    )
    @GetMapping("/{id}")
    public CardResponse getById(@PathVariable("id")Integer id){
        return new CardResponse(this.cardService.getById(id));
    }

    @Operation(
            description = "Returns the card with given serial number.",
            summary = "Get card using serial number"
    )
    @GetMapping
    public CardResponse getBySerialNumber(@RequestBody SerialNumberRequest request){
        return new CardResponse(this.cardService.getBySerialNumber(request.getSerialNumber()));
    }

    @Operation(
            description = "Deletes the card with the given ID",
            summary = "Delete card"
    )
    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable("id")Integer id){
        this.cardService.deleteCard(id);
    }
}
