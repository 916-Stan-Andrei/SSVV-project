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

    private Validator<Tema> temaValidator;

    private TemaXMLRepository temaXMLRepository;

    private Service service;

    private UI ui;

    @Before
    public void setUp(){
        temaValidator = new Validator<Tema>() {
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

        temaXMLRepository = new TemaXMLRepository(temaValidator, "testTema");
        service = new Service(null, temaXMLRepository, null);
        ui = new UI(service);
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
    public void testAddAssignment_deadlineAndStartlineNotInTheValidRange_assignmentNotAdded(){
        assertEquals(0, service.saveTema("1", "descriere1", 15, 1));
        assertEquals(0, service.saveTema("2", "descriere2", 13, 0));
    }

}
