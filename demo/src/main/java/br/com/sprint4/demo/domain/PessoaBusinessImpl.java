package br.com.sprint4.demo.domain;

import br.com.sprint4.demo.repository.PessoaRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PessoaBusinessImpl implements PessoaBusiness  {

    private PessoaRepository pessoaRepository;

    public PessoaBusinessImpl(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }
    @Override
    public Pessoa save(Pessoa pessoa) {
        pessoa.setId(generateId());
        return pessoaRepository.save(pessoa);
    }

    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
