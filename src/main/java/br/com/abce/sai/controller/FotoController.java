package br.com.abce.sai.controller;

import br.com.abce.sai.converter.ImageConverter;
import br.com.abce.sai.exception.InfraestructureException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.Foto;
import br.com.abce.sai.persistence.repo.FotoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/foto")
@Api
@CrossOrigin(origins = "http://localhost:4200")
public class FotoController {

    private FotoRepository fotoRepository;

    private ImageConverter imageConverter;

    public FotoController(FotoRepository fotoRepository, ImageConverter imageConverter) {
        this.fotoRepository = fotoRepository;
        this.imageConverter = imageConverter;
    }

    @ApiOperation(value = "Consulta uma foto.")
    @GetMapping(value = "{id}", produces = {MediaType.IMAGE_JPEG_VALUE,
                                                MediaType.IMAGE_PNG_VALUE,
                                                MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> findByOne(
            @PathVariable @NotNull(message = "Id da foto obrigatório.")
            @NotNull(message = "foto é obrigatório.") Long id,
            @RequestParam(required = false) @Min(message = "Valor mínimo de 30px", value = 30) Integer with,
            @RequestParam(required = false) @Max(message = "Valor máximo de 300px", value = 300) Integer lenght) {

        Foto foto = fotoRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Foto.class, id));

        if (with != null && lenght != null) {

            byte[] reSizedImage = getReSizedImage(foto, with, lenght);

            foto.setImagem(reSizedImage);
        }

        return ResponseEntity.ok(foto.getImagem());
    }



    @ApiOperation(value = "Upload de uma foto.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<Foto>> createFoto(@RequestBody MultipartFile multipartFile) {

        Foto foto = getFoto(multipartFile);

        Foto fotoSaved = fotoRepository.save(foto);

        EntityModel<Foto> fotoModel = EntityModel.of(fotoSaved,
                linkTo(methodOn(FotoController.class).findByOne(fotoSaved.getIdFoto(), null, null)).withSelfRel());

        return ResponseEntity.created(fotoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(fotoModel);
    }

    @ApiOperation(value = "Deleta foto do imóvel.")
    @DeleteMapping("/{id}")
    public HttpEntity<Object> delete(@PathVariable @NotNull(message = "Id da foto é obrigatório.") Long id) {

        fotoRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Foto.class, id));
        fotoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private byte[] getReSizedImage(Foto foto, Integer with, Integer height) {

        try {

            InputStream is = new ByteArrayInputStream(foto.getImagem());
            BufferedImage bi = ImageIO.read(is);

            bi = imageConverter.resizeImageGr(bi, with, height);

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            ImageIO.write(bi, foto.getTipo().split("/")[1], output);

            return output.toByteArray();

        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex.getSuppressed());
            throw new InfraestructureException(ex);
        }

    }

    private Foto getFoto(MultipartFile multipartFile) {

        try {

            Foto foto = new Foto();
            foto.setTipo(multipartFile.getContentType());
            foto.setImagem(multipartFile.getBytes());
            foto.setNameFile(multipartFile.getOriginalFilename());

            return foto;

        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                    e.getMessage(),
                    e.getSuppressed());
            throw new InfraestructureException(e);
        }
    }
}
