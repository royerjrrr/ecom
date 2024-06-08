package com.ConexionS.Controller;

import com.ConexionS.Entities.Sneakers;
import com.ConexionS.Service.SneakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sneakers")
public class SneakerController {

    @Autowired
    private SneakerService sneakerService;

    @PostMapping("/add-sneakers")
    public ResponseEntity<Sneakers> createSneaker(@RequestBody Sneakers sneakers) {
        Sneakers saveSneaker = sneakerService.createProduct(sneakers);
        return new ResponseEntity<>(saveSneaker, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-sneakers")
    public ResponseEntity<List<Sneakers>> getAllSneaker() {
        List<Sneakers> sneakers = sneakerService.getAllSneakers();
        return new ResponseEntity<>(sneakers, HttpStatus.OK);
    }

    @GetMapping("/get-sneaker/{id}")
    public ResponseEntity<Sneakers> getSneakerById(@PathVariable Integer id) {
        Optional<Sneakers> sneakers = sneakerService.getSneakerById(id);
        return sneakers.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sneakers> updateSneaker(@PathVariable Integer id, @RequestBody(required = false) Sneakers sneakersDetails) {
        Optional<Sneakers> sneakersOptional = sneakerService.getSneakerById(id);

        if (sneakersOptional.isPresent()) {
            Sneakers sneakers = sneakersOptional.get();

            if(sneakersDetails != null && sneakersDetails.getName() != null && sneakersDetails.getBrand() != null && sneakersDetails.getIconicLine() != null
                    && sneakersDetails.getAmount() != null && sneakersDetails.getColor() != null && sneakersDetails.getDescription() != null
                    && sneakersDetails.getCategory() != null && sneakersDetails.getSize() != null && sneakersDetails.getPrice() != null && sneakersDetails.getStatus() != null) {
                sneakers.setName(sneakersDetails.getName());
                sneakers.setBrand(sneakersDetails.getBrand());
                sneakers.setIconicLine(sneakersDetails.getIconicLine());
                sneakers.setAmount(sneakersDetails.getAmount());
                sneakers.setColor(sneakersDetails.getColor());
                sneakers.setDescription(sneakersDetails.getDescription());
                sneakers.setCategory(sneakersDetails.getCategory());
                sneakers.setSize(sneakersDetails.getSize());
                sneakers.setPrice(sneakersDetails.getPrice());
                sneakers.setStatus(sneakersDetails.getStatus());

                Sneakers updateSneaker = sneakerService.updateSneaker(sneakers);
                return new ResponseEntity<>(updateSneaker, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSneaker(@PathVariable Integer id) {
        if (sneakerService.getSneakerById(id).isPresent()) {
            sneakerService.deleteSneaker(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-image/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer id) {
        try {
            sneakerService.deleteImage(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Integer id) {
        try {
            String imagePath = sneakerService.obtenerRutaImagen(id);
            Path file = Paths.get(imagePath);
            if (!Files.exists(file) || !Files.isReadable(file)) {
                throw new RuntimeException("No se puede leer el archivo: " + imagePath);
            }
            Resource resource = new UrlResource(file.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName().toString() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}