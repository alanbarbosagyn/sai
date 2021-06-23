package br.com.abce.sai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchCriteriaDto {
    private String key;
    private String operation;
    private Object value;
    private boolean orPredicate;

    public String getKey() {
        String dePara = key;
        switch (key) {
            case "tipo":
                dePara = "tipoImovelByTipoImovelIdTipoImovel";
                break;
        }
        return dePara;
    }
}
