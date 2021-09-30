package br.com.abce.sai.service;


import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.persistence.model.Municipio;
import br.com.abce.sai.persistence.repo.MunicipioRepository;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MunicipioService {

    private MunicipioRepository municipioRepository;

    public MunicipioService(MunicipioRepository municipioRepository) {
        this.municipioRepository = municipioRepository;
    }

    public Municipio getMunicipio(Municipio municipio) {

        if (municipio == null
                || ((municipio.getIdMunicipio() == null || municipio.getIdMunicipio() == 0L)
                && StringUtils.isBlank(municipio.getCodgIbge())))
            throw new DataValidationException("Id do Municipio ou código IBGE é obrigatório");

        return municipioRepository.findMunicipioByIdMunicipioOrCodgIbge(municipio.getIdMunicipio(), municipio.getCodgIbge())
                .orElseThrow(() -> new DataValidationException("Município não encontrado."));
    }

}
