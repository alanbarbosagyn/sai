package br.com.abce.sai.controller;

import br.com.abce.sai.converter.ImageConverter;
import br.com.abce.sai.converter.impl.ImageCompress;
import br.com.abce.sai.exception.InfraestructureException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.Foto;
import br.com.abce.sai.persistence.repo.FotoRepository;
import br.com.abce.sai.util.LoggerUtil;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/foto")
@Api
public class FotoController {

    private FotoRepository fotoRepository;

    private ImageConverter imageConverter;

    private ImageCompress imageCompress;

    public FotoController(FotoRepository fotoRepository, ImageConverter imageConverter, ImageCompress imageCompress) {
        this.fotoRepository = fotoRepository;
        this.imageConverter = imageConverter;
        this.imageCompress = imageCompress;
    }

    @ApiOperation(value = "Consulta uma foto.")
    @GetMapping(value = "{id}", produces = {MediaType.IMAGE_JPEG_VALUE,
                                                MediaType.IMAGE_PNG_VALUE,
                                                MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> findByOne(
            @PathVariable @NotNull(message = "Id da foto obrigatório.")
            @NotNull(message = "foto é obrigatório.") Long id,
            @RequestParam(required = false) @Min(message = "Valor mínimo de 30px", value = 30) Integer width) {

        Foto foto = fotoRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Foto.class, id));

        if (width != null) {

            byte[] reSizedImage = getReSizedImage(foto, width);

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
                linkTo(methodOn(FotoController.class).findByOne(fotoSaved.getIdFoto(), null)).withSelfRel());

        return ResponseEntity.created(fotoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(fotoModel);
    }

    @ApiOperation(value = "Deleta foto.")
    @DeleteMapping("/{id}")
    public HttpEntity<Object> delete(@PathVariable @NotNull(message = "Id da foto é obrigatório.") Long id) {

        fotoRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Foto.class, id));

        fotoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private byte[] getReSizedImage(Foto foto, Integer with) {

        try {

            InputStream is = new ByteArrayInputStream(foto.getImagem());
            BufferedImage bi = ImageIO.read(is);

            int height = getHeightProportional(bi, with);

            bi = imageConverter.resizeImageGr(bi, with, height);

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            ImageIO.write(bi, foto.getTipo().split("/")[1], output);

            return output.toByteArray();

        } catch (IOException ex) {
            LoggerUtil.error(ex.getMessage());
            throw new InfraestructureException(ex);
        }

    }

    private Integer getHeightProportional(BufferedImage bi, Integer with) {

        float biWidth = (float) bi.getWidth();
        final float proportion = with > biWidth ? with/ biWidth : biWidth /with;

        return (int) (with > biWidth ? bi.getHeight() * proportion : bi.getHeight()/proportion);
    }

    private Foto getFoto(MultipartFile multipartFile) {

        try {

            InputStream is = new ByteArrayInputStream(multipartFile.getBytes());
            BufferedImage bi = ImageIO.read(is);

            byte[] imageCompressBytes = imageCompress.compressTo(bi, multipartFile.getContentType().split("/")[1]);

            Foto foto = new Foto();
            foto.setTipo(multipartFile.getContentType());
            foto.setImagem(imageCompressBytes);
            foto.setNameFile(multipartFile.getOriginalFilename());

            return foto;

        } catch (IOException e) {
            LoggerUtil.error(e.getMessage());
            throw new InfraestructureException(e);
        }
    }
}
