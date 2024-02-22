package com.dopaming.dopaming.controller;

import com.dopaming.dopaming.domain.Pamings;
import com.dopaming.dopaming.service.PamingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pamings")
public class PamingsController {

    @Autowired
    private PamingsService pamingsService;

    @GetMapping
    public List<Pamings> getAllPamings(){
        return pamingsService.getAllPamings();
    }

    @GetMapping("/{paming_id}")
    public ResponseEntity<Pamings> getPamingById(@PathVariable("paming_id") Long paming_id){
        Pamings pamings = pamingsService.getPamingById(paming_id);
        if(pamings != null){
            return ResponseEntity.ok(pamings);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Pamings> createPaming(@RequestBody Pamings pamings){
        Pamings createdPaming = pamingsService.createPaming(pamings);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPaming);
    }

    @PutMapping("/{paming_id}")
    public ResponseEntity<Pamings> updatePaming(@PathVariable("paming_id") Long paming_id, @RequestBody Pamings updatedPaming){
        Pamings pamings = pamingsService.updatePaming(paming_id, updatedPaming);
        if (pamings != null){
            return ResponseEntity.ok(pamings);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
