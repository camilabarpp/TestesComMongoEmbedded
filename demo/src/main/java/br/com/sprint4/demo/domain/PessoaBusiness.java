package br.com.sprint4.demo.domain;

import org.reactivestreams.Publisher;

public interface PessoaBusiness {
    Pessoa save(Pessoa pessoa);

    String generateId();
}
