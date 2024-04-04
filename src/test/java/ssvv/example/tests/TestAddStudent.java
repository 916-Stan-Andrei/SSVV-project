package ssvv.example.tests;


import org.junit.Before;
import org.junit.Test;
import ssvv.example.domain.Student;
import ssvv.example.repository.StudentXMLRepository;
import ssvv.example.service.Service;
import ssvv.example.validation.ValidationException;
import ssvv.example.validation.Validator;

import static junit.framework.Assert.assertEquals;

public class TestAddStudent {
    private Validator<Student> studentValidator;
    private StudentXMLRepository studentXMLRepository;
    private Service service;

    @Before
    public void setUp() {
        studentValidator = new Validator<Student>() {
            @Override
            public void validate(Student student) throws ValidationException {
                if (student.getID() == null || student.getID().equals("")) {
                    throw new ValidationException("ID invalid! \n");
                }
                if (student.getNume() == null || student.getNume().equals("")) {
                    throw new ValidationException("Nume invalid! \n");
                }
                if (student.getGrupa() <= 110 || student.getGrupa() >= 938) {
                    throw new ValidationException("Grupa invalida! \n");
                }
            }
        };
        studentXMLRepository = new StudentXMLRepository(studentValidator, "testStudent");
        service = new Service(studentXMLRepository, null, null);
    }

    @Test
    public void testAddStudent_grupaValida() {
        assertEquals(service.saveStudent("0", "Bob", 211), 0);
        assertEquals(service.saveStudent("1", "Bob", 212), 0);
        assertEquals(service.saveStudent("2", "Bob", 213), 0);
    }

    @Test
    public void testAddStudent_grupaInvalida() {
        assertEquals(service.saveStudent("0", "Bob", -2), 1);
        assertEquals(service.saveStudent("1", "Bob", 4000), 1);
    }
}
