package fullstack.cresol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fullstack.cresol.exception.TypeInstituionConflictException;
import fullstack.cresol.exception.TypeInstitutionNotFoundException;
import fullstack.cresol.model.TypeInstitution;
import fullstack.cresol.repository.TypeInstitutionRepository;

@ExtendWith(MockitoExtension.class)
public class TypeInstitutionServiceTests {

	@InjectMocks
	private TypeInstitutionService service;

	@Mock
	private TypeInstitutionRepository repository;

	@DisplayName("TypeInstituition - POST - Create type and prevent duplicate")
	@Test
	void createTypePreventDuplicate() {
		TypeInstitution typeInstitution = new TypeInstitution();
		typeInstitution.setId(1);
		typeInstitution.setName("Baser");

		// Simula que o nome "Baser" ainda NÃO existe no banco
		when(repository.findByName("Baser"))
			.thenReturn(Optional.empty());

		// Simula a criação do novo tipo com sucesso
		when(repository.save(any(TypeInstitution.class)))
			.thenReturn(typeInstitution);

		// Primeiro: Criar com sucesso
		TypeInstitution created = service.addNewType(typeInstitution);

		assertNotNull(created);
		assertEquals(1, created.getId());
		assertEquals("Baser", created.getName());

		// Agora, simula que "Baser" JÁ EXISTE no banco para o segundo teste
		when(repository.findByName("Baser"))
			.thenReturn(Optional.of(typeInstitution));

		TypeInstitution newTypeInstitution = new TypeInstitution();
		newTypeInstitution.setId(2);
		newTypeInstitution.setName("Baser");

		// Segundo: Impedir criação de tipo duplicado
		Exception exception = assertThrows(
			TypeInstituionConflictException.class, 
			() -> service.addNewType(newTypeInstitution)
		);

		assertEquals("Tipo Baser já cadastrado.", exception.getMessage());

		// Verifica que o método save foi chamado apenas UMA VEZ (para o primeiro tipo)
		verify(repository, times(1)).save(any(TypeInstitution.class));

		// Verifica que o método save NÃO foi chamado na segunda tentativa
		verify(repository, never()).save(newTypeInstitution);
	}

	@DisplayName("TypeInstitution - GET - find all type")
	@Test
	void findAllTypeResult() {
		List<TypeInstitution> typeInstitutions = Arrays.asList(
				new TypeInstitution(1, "Banco"),
				new TypeInstitution(2, "Cooperativa")
			);

			when(repository.findAll()).thenReturn(typeInstitutions);

			List<TypeInstitution> result = (List<TypeInstitution>) service.getAllTypeInstitution();

			// Assert
			assertNotNull(result);
			assertEquals(2, result.size());
			assertEquals("Banco", result.get(0).getName());
			assertEquals("Cooperativa", result.get(1).getName());

			verify(repository, times(1)).findAll();
	}

	@DisplayName("TypeInstitution - GET - find all type - empty list")
	@Test
	void findAllTypeNotResult() {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		List<TypeInstitution> result = (List<TypeInstitution>) service.getAllTypeInstitution();

		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(repository, times(1)).findAll();
	}

	@DisplayName("TypeInstituition - GET - find type by ID")
	@Test
	void findTypeByID() {
		TypeInstitution typeInstitution = new TypeInstitution();
		typeInstitution.setId(1);
		typeInstitution.setName("Baser");

		// Simula que o ID 1 existe no banco
		when(repository.findById(1))
			.thenReturn(Optional.of(typeInstitution));

		// Testa busca com sucesso
		TypeInstitution found = service.getTypeInstitution(1);

		assertNotNull(found);
		assertEquals(1, found.getId());
		assertEquals("Baser", found.getName());

		// Simula que o ID 2 NÃO existe no banco
		when(repository.findById(2))
			.thenReturn(Optional.empty());

		// Testa busca com ID inexistente
		Exception exception = assertThrows(
				TypeInstitutionNotFoundException.class, 
			() -> service.getTypeInstitution(2)
		);
		System.out.println(exception);
		assertEquals("Não existe tipo cadastrado com o código 2.", exception.getMessage());

		// Verifica que o método findById foi chamado corretamente
		verify(repository, times(1)).findById(1);
		verify(repository, times(1)).findById(2);
	}

	@DisplayName("TypeInstituition - PUT - update a type successfully")
	@Test
	void updateTypeSucessfully() {
		TypeInstitution existingType = new TypeInstitution();
		existingType.setId(1);
		existingType.setName("Original");

		TypeInstitution updatedType = new TypeInstitution();
		updatedType.setId(1);
		updatedType.setName("Atualizado");

		when(repository.findById(1)).thenReturn(Optional.of(existingType));
		when(repository.existsByName("Atualizado")).thenReturn(false);
		when(repository.save(any(TypeInstitution.class))).thenReturn(updatedType);

		TypeInstitution result = service.updateTypeInstitution(updatedType);

		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("Atualizado", result.getName());

		verify(repository, times(1)).findById(1);
		verify(repository, times(1)).existsByName("Atualizado");
		verify(repository, times(1)).save(any(TypeInstitution.class));
	}

	@DisplayName("TypeInstituition - PUT - update type exception type does not exist")
	@Test
	void updateTypeExceptionTypeNotExist() {
		TypeInstitution updatedType = new TypeInstitution();
		updatedType.setId(99);
		updatedType.setName("Novo Nome");

		when(repository.findById(99)).thenReturn(Optional.empty());

		Exception exception = assertThrows(TypeInstitutionNotFoundException.class, () -> 
			service.updateTypeInstitution(updatedType)
		);

		assertEquals("Não existe tipo cadastrado com o código 99", exception.getMessage());

		verify(repository, times(1)).findById(99);
		verify(repository, never()).existsByName(anyString());
		verify(repository, never()).save(any(TypeInstitution.class));
	}

	@DisplayName("TypeInstituition - PUT - update type exception name already exists")
	@Test
	void updateTypeExceptionNameAlreadyExists() {
		TypeInstitution existingType = new TypeInstitution();
		existingType.setId(1);
		existingType.setName("Original");

		TypeInstitution updatedType = new TypeInstitution();
		updatedType.setId(1);
		updatedType.setName("NomeExistente");

		when(repository.findById(1)).thenReturn(Optional.of(existingType));
		when(repository.existsByName("NomeExistente")).thenReturn(true);

		Exception exception = assertThrows(TypeInstituionConflictException.class, () -> 
			service.updateTypeInstitution(updatedType)
		);

		assertEquals("Tipo NomeExistente já cadastrado.", exception.getMessage());

		verify(repository, times(1)).findById(1);
		verify(repository, times(1)).existsByName("NomeExistente");
		verify(repository, never()).save(any(TypeInstitution.class));
	}
}
