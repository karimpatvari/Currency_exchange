package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyEntity {
    private Integer ID;
    private String Code;
    private String FullName;
    private String Sign;
}
