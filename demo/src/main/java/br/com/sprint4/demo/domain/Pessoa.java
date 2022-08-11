package br.com.sprint4.demo.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class Pessoa {

    @Id
    private String id;
    private String nome;
    private String cpf;
}
