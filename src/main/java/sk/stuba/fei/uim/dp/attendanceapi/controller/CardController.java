package sk.stuba.fei.uim.dp.attendanceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.dp.attendanceapi.request.NameRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SerialNumberRequest;
import sk.stuba.fei.uim.dp.attendanceapi.response.CardResponse;
import sk.stuba.fei.uim.dp.attendanceapi.service.CardService;

@RestController
@RequestMapping("/api/card")
@Tag(name = "Card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
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
    public CardResponse getBySerialNumber(@Valid @RequestBody SerialNumberRequest request){
        return new CardResponse(this.cardService.getBySerialNumber(request.getSerialNumber()));
    }

    @Operation(
            description = "Updates the name of the card with given id",
            summary = "Update card"
    )
    @PutMapping("/{id}")
    public CardResponse update(@PathVariable("id")Integer id, @Valid @RequestBody NameRequest request){
        return new CardResponse(this.cardService.update(request, id));
    }

    @Operation(
            description = "Deactivates the card with given id.",
            summary = "Deactivate card"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateCard(@PathVariable("id")Integer id){
        this.cardService.deactivateCard(id);
        return new ResponseEntity<>("Card successfully deactivated.", HttpStatus.OK);
    }
}
