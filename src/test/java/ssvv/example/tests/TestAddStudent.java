package ssvv.example.tests;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ssvv.example.domain.Student;
import ssvv.example.repository.StudentXMLRepository;
import ssvv.example.service.Service;
import ssvv.example.validation.ValidationException;
import ssvv.example.validation.Validator;

import static org.junit.Assert.assertEquals;


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

    @After
    public void tearDown() {
        studentXMLRepository.clearAllStudents();
    }

    @Test
    public void testAddStudent_nameIsAlphabetical_addsStudentCorrectly(){
        assertEquals(1, service.saveStudent("1", "Bob", 211));
        assertEquals(1, service.saveStudent("2", "Maria", 211));
        assertEquals(1, service.saveStudent("3", "Ion", 211));
    }

    @Test
    public void testAddStudent_nameContainsIntegers_studentNotAdded(){
        assertEquals(0, service.saveStudent("0", "B1b", 211));
        assertEquals(0, service.saveStudent("1", "1112", 212));
        assertEquals(0, service.saveStudent("2", "342687", 213));
    }

    @Test
    public void testAddStudent_idIsNaturalNumberLessThanMaxInt_addsStudentCorrectly(){
        assertEquals(1, service.saveStudent("0", "Bob", 211));
        assertEquals(1, service.saveStudent("1", "Bob", 212));
        assertEquals(1, service.saveStudent("2", "Bob", 213));
    }

    @Test
    public void testAddStudent_idIsNotNaturalNumber_studentNotAdded(){
        assertEquals(0, service.saveStudent("-1", "Bob", 211));
        assertEquals(0, service.saveStudent("2.5", "Bob", 212));
    }


    @Test
    public void testAddStudent_studentIdAlreadyExists_studentNotAdded() {
        String studentId = "123";
        service.saveStudent(studentId, "Alice", 123);

        assertEquals(0, service.saveStudent(studentId, "Bob", 456));
    }

    @Test
    public void testAddStudent_groupIsNaturalNumberInTheGivenRange_addsStudentCorrectly() {
        assertEquals(1, service.saveStudent("0", "Bob", 211));
        assertEquals(1, service.saveStudent("1", "Bob", 212));
        assertEquals(1, service.saveStudent("2", "Bob", 213));
    }

    @Test
    public void testAddStudent_groupIsNegativeNumber_studentNotAdded() {
        assertEquals(0, service.saveStudent("0", "Bob", -2));
        assertEquals(0, service.saveStudent("0", "Bob", -500));
    }

    @Test
    public void testAddStudent_groupIsNegativeNumberGreaterThanMaxInt_studentNotAdded() {
        assertEquals(0, service.saveStudent("4", "Bob", Integer.MAX_VALUE + 1));
    }

    //Special BVA

    @Test
    public void testAddStudent_idLessOrEqualThenMaxInt_addsStudentCorrectly(){
        assertEquals(1, service.saveStudent("2147483647", "Bob", 211));
        assertEquals(1, service.saveStudent("2147483646", "Bob", 211));
    }

    @Test
    public void testAddStudent_idIsNaturalNumberGreaterThanMaxInt_studentNotAdded(){
        assertEquals(0, service.saveStudent("2147483648", "Bob", 211));
    }

    @Test
    public void testAddStudent_groupWithValidBoundaries_addsStudentCorrectly(){
        assertEquals(1, service.saveStudent("1", "Bob", 111));
        assertEquals(1, service.saveStudent("2", "Bob", 937));
    }
    @Test
    public void testAddStudent_groupWithInvalidBoundaries_studentNotAdded(){
        assertEquals(0, service.saveStudent("1", "Bob", 110));
        assertEquals(0, service.saveStudent("2", "Bob", 938));
    }
}
