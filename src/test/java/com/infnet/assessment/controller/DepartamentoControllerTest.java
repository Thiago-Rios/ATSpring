package com.infnet.assessment.controller;

import com.infnet.assessment.models.Departamento;
import com.infnet.assessment.repositories.DepartamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartamentoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private HttpHeaders headers;

    @BeforeEach
    public void setup() {
        departamentoRepository.deleteAll();

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
    public void testCreateDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNome("Financeiro");
        departamento.setLocal("Prédio B");

        HttpEntity<Departamento> request = new HttpEntity<>(departamento, headers);
        ResponseEntity<Departamento> response = restTemplate.postForEntity("/api/departamentos", request, Departamento.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    public void testGetDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNome("Recursos Humanos");
        departamento.setLocal("Prédio A");

        departamento = departamentoRepository.save(departamento);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Departamento> response = restTemplate.exchange("/api/departamentos/" + departamento.getId(), HttpMethod.GET, request, Departamento.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getNome()).isEqualTo("Recursos Humanos");
    }

    @Test
    public void testUpdateDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNome("Tecnologia");
        departamento.setLocal("Prédio C");

        departamento = departamentoRepository.save(departamento);

        departamento.setNome("Tecnologia da Informação");
        HttpEntity<Departamento> requestUpdate = new HttpEntity<>(departamento, headers);
        ResponseEntity<Departamento> response = restTemplate.exchange("/api/departamentos/" + departamento.getId(), HttpMethod.PUT, requestUpdate, Departamento.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getNome()).isEqualTo("Tecnologia da Informação");
    }

    @Test
    public void testDeleteDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNome("Marketing");
        departamento.setLocal("Prédio D");

        departamento = departamentoRepository.save(departamento);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange("/api/departamentos/" + departamento.getId(), HttpMethod.DELETE, request, Void.class);

        ResponseEntity<Departamento> response = restTemplate.exchange("/api/departamentos/" + departamento.getId(), HttpMethod.GET, request, Departamento.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void testGetAllDepartamentos() {
        Departamento departamento1 = new Departamento();
        departamento1.setNome("Financeiro");
        departamento1.setLocal("Prédio B");
        departamentoRepository.save(departamento1);

        Departamento departamento2 = new Departamento();
        departamento2.setNome("Logística");
        departamento2.setLocal("Prédio C");
        departamentoRepository.save(departamento2);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Departamento[]> response = restTemplate.exchange("/api/departamentos", HttpMethod.GET, request, Departamento[].class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].getNome()).isEqualTo("Financeiro");
        assertThat(response.getBody()[1].getNome()).isEqualTo("Logística");
    }
}
