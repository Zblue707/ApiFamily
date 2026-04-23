package com.example.apifamilia;

import com.example.apifamilia.repository.IntegranteRepository;
import com.example.apifamilia.repository.UsuarioGoogleRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=" +
				"org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
				"org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration"
})
class ApifamiliaApplicationTests {

	@MockitoBean
	IntegranteRepository integranteRepository;

	@MockitoBean
	UsuarioGoogleRepository usuarioGoogleRepository;

	@Test
	void contextLoads() {
	}

}
