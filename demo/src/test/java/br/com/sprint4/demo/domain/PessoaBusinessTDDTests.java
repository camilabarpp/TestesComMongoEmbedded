package br.com.sprint4.demo.domain;

import br.com.sprint4.demo.MongoConfig;
import br.com.sprint4.demo.repository.PessoaRepository;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import de.flapdoodle.embed.mongo.MongodExecutable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@Import(MongoConfig.class)
@ActiveProfiles("test")
@SpringBootTest(classes = PessoaBusinessImpl.class)
@EnableMongoRepositories(basePackageClasses = PessoaRepository.class)
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.4.6")
public class PessoaBusinessTDDTests {

    public static final String MOCK_ID = "1";
    Pessoa pessoaSalva = Pessoa.builder()
            .nome("Camila")
            .cpf("123456")
            .build();

    @SpyBean
    private PessoaBusiness pessoaBusiness;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired private MongoTemplate mongoTemplate;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("Deve salvar uma pessoa.")
    public void saveBookTest() {

        when(pessoaBusiness.generateId()).thenReturn(MOCK_ID);
        pessoaBusiness.save(pessoaSalva);
        assertThat(pessoaSalva.getId().equals(MOCK_ID));
        //execucao
        mongoTemplate.save(pessoaSalva, "collection");

        //verificacoes
        assertThat(pessoaSalva.getId()).isNotNull();
        assertThat(mongoTemplate.findAll(Pessoa.class, "collection"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("Teste de geraçao do ID")
    void testPessoaId() {

        pessoaBusiness.save(pessoaSalva);
        assertThat(pessoaSalva.getId()).isNotNull();

    }

    @Test
    @DisplayName("Deve obter uma pessoa por id.")
    public void findByIdTest(){
        //cenário
        Pessoa book = Pessoa.builder()
                .id("3")
                .nome("2345")
                .cpf("345")
                .build();
        mongoTemplate.save(book, "collection");

        //execucao
        Optional<Pessoa> foundBook = pessoaRepository.findById(book.getId());

        //verificacoes
        assertThat(foundBook.isPresent()).isTrue();
    }

    @DisplayName("Given object When save object using MongoDB template Then object can be found")
    @Test
    public void test(@Autowired MongoTemplate mongoTemplate) {
        // given
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("key", "value")
                .get();

        // when
        mongoTemplate.save(objectToSave, "testando");

        // then
        assertThat(mongoTemplate.findAll(DBObject.class, "testando")).extracting("key")
                .containsOnly("value");
    }

}
