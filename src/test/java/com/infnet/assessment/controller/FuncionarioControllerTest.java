package com.infnet.assessment.controller;

import com.infnet.assessment.models.Funcionario;
import com.infnet.assessment.repositories.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FuncionarioControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private HttpHeaders headers;

    @BeforeEach
    public void setup() {
        funcionarioRepository.deleteAll();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", "admin");
        form.add("password", "admin");

        HttpEntity<MultiValueMap<String, String>> loginRequest = new HttpEntity<>(form, headers);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", loginRequest, String.class);

        List<String> cookies = loginResponse.getHeaders().get("Set-Cookie");
        headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
    }

    @Test
    public void testCreateFuncionario() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Carlos Souza");
        funcionario.setEndereco("Rua Principal, 456");
        funcionario.setTelefone("987654321");
        funcionario.setEmail("carlos.souza@example.com");
        funcionario.setDataNascimento(LocalDate.parse("1985-05-10"));

        HttpEntity<Funcionario> request = new HttpEntity<>(funcionario, headers);
        ResponseEntity<Funcionario> response = restTemplate.postForEntity("/api/funcionarios", request, Funcionario.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    public void testGetFuncionario() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Ana Silva");
        funcionario.setEndereco("Avenida Brasil, 123");
        funcionario.setTelefone("123456789");
        funcionario.setEmail("ana.silva@example.com");
        funcionario.setDataNascimento(LocalDate.parse("1990-02-15"));

        funcionario = funcionarioRepository.save(funcionario);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Funcionario> response = restTemplate.exchange("/api/funcionarios/" + funcionario.getId(), HttpMethod.GET, request, Funcionario.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getNome()).isEqualTo("Ana Silva");
    }

    @Test
    public void testUpdateFuncionario() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Paulo Lima");
        funcionario.setEndereco("Rua das Palmeiras, 789");
        funcionario.setTelefone("1122334455");
        funcionario.setEmail("paulo.lima@example.com");
        funcionario.setDataNascimento(LocalDate.parse("1978-12-01"));

        funcionario = funcionarioRepository.save(funcionario);

        funcionario.setNome("Paulo Lima Atualizado");
        HttpEntity<Funcionario> requestUpdate = new HttpEntity<>(funcionario, headers);
        ResponseEntity<Funcionario> response = restTemplate.exchange("/api/funcionarios/" + funcionario.getId(), HttpMethod.PUT, requestUpdate, Funcionario.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getNome()).isEqualTo("Paulo Lima Atualizado");
    }

    @Test
    public void testDeleteFuncionario() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Juliana Costa");
        funcionario.setEndereco("Rua do Comércio, 321");
        funcionario.setTelefone("5566778899");
        funcionario.setEmail("juliana.costa@example.com");
        funcionario.setDataNascimento(LocalDate.parse("1995-07-07"));

        funcionario = funcionarioRepository.save(funcionario);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange("/api/funcionarios/" + funcionario.getId(), HttpMethod.DELETE, request, Void.class);

        ResponseEntity<Funcionario> response = restTemplate.exchange("/api/funcionarios/" + funcionario.getId(), HttpMethod.GET, request, Funcionario.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void testGetAllFuncionarios() {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNome("Lucas Ferreira");
        funcionario1.setEndereco("Rua do Mercado, 123");
        funcionario1.setTelefone("123123123");
        funcionario1.setEmail("lucas.ferreira@example.com");
        funcionario1.setDataNascimento(LocalDate.parse("1980-01-01"));
        funcionarioRepository.save(funcionario1);

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Mariana Souza");
        funcionario2.setEndereco("Avenida Central, 456");
        funcionario2.setTelefone("456456456");
        funcionario2.setEmail("mariana.souza@example.com");
        funcionario2.setDataNascimento(LocalDate.parse("1990-02-02"));
        funcionarioRepository.save(funcionario2);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Funcionario[]> response = restTemplate.exchange("/api/funcionarios", HttpMethod.GET, request, Funcionario[].class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].getNome()).isEqualTo("Lucas Ferreira");
        assertThat(response.getBody()[1].getNome()).isEqualTo("Mariana Souza");
    }
}
