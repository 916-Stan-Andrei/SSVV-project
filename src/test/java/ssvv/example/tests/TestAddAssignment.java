package ssvv.example.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ssvv.example.console.UI;
import ssvv.example.domain.Tema;
import ssvv.example.repository.TemaXMLRepository;
import ssvv.example.service.Service;
import ssvv.example.validation.ValidationException;
import ssvv.example.validation.Validator;

import static org.junit.Assert.*;

public class TestAddAssignment {

    private TemaXMLRepository temaXMLRepository;

    private Service service;

    @Before
    public void setUp(){
        Validator<Tema> temaValidator = new Validator<Tema>() {
            @Override
            public void validate(Tema tema) throws ValidationException {
                if (tema.getID() == null || tema.getID().equals("")) {
                    throw new ValidationException("ID invalid! \n");
                }
                if (tema.getDescriere() == null || tema.getDescriere().equals("")) {
                    throw new ValidationException("Descriere invalida! \n");
                }
                if (tema.getDeadline() < 1 || tema.getDeadline() > 14 || tema.getDeadline() < tema.getStartline()) {
                    throw new ValidationException("Deadline invalid! \n");
                }
                if (tema.getStartline() < 1 || tema.getStartline() > 14 || tema.getStartline() > tema.getDeadline()) {
                    throw new ValidationException("Data de primire invalida! \n");
                }
            }
        };

        temaXMLRepository = new TemaXMLRepository(temaValidator, "testTema.xml");
        service = new Service(null, temaXMLRepository, null);
        UI ui = new UI(service);
    }

    @After
    public void tearDown(){
        temaXMLRepository.clearAllAssignments();
    }

    @Test
    public void testAddAssignment_deadlineAndStartlineInTheValidRange_assignmentSuccessfullyAdded(){
        assertEquals(1, service.saveTema("1", "descriere1", 14, 1));
        assertEquals(1, service.saveTema("2", "descriere2", 13, 2));
    }

    @Test
    public void testAddAssignment_startlineLowerThanTheLowerBound_throwsException(){
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.saveTema("2", "descriere2", 13, 0);
        });
        assertEquals("Data de primire invalida! \n", exception.getMessage());
    }

    @Test
    public void testAddAssignment_startlineGreaterThanTheUpperBound_throwsException(){
        // This test satisfies also the situation when deadline is less than startline
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.saveTema("2", "descriere2", 3, 15);
        });
        assertEquals("Deadline invalid! \n", exception.getMessage());
    }

    @Test
    public void testAddAssignment_deadlineLowerThanTheLowerBound_throwsException(){
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.saveTema("2", "descriere2", 0, 12);
        });
        assertEquals("Deadline invalid! \n", exception.getMessage());
    }

    @Test
    public void testAddAssignment_deadlineGreaterThanTheUpperBound_throwsException(){
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.saveTema("2", "descriere2", 15, 12);
        });
        assertEquals("Deadline invalid! \n", exception.getMessage());
    }

    @Test
    public void testAddAssignment_nullDescription_throwsException(){
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.saveTema("2", null, 5, 7);
        });
        assertEquals("Descriere invalida! \n", exception.getMessage());
    }

    @Test
    public void testAddAssignment_emptyDescription_throwsException(){
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.saveTema("2", "", 5, 7);
        });
        assertEquals("Descriere invalida! \n", exception.getMessage());
    }

    @Test
    public void testAddAssignment_nullId_throwsException(){
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.saveTema(null, "desc", 5, 7);
        });
        assertEquals("ID invalid! \n", exception.getMessage());
    }

    @Test
    public void testAddAssignment_emptyStringId_throwsException(){
        Exception exception = assertThrows(ValidationException.class, () -> {
            service.saveTema("", "desc", 5, 7);
        });
        assertEquals("ID invalid! \n", exception.getMessage());
    }



}
