package ssvv.example.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepository;
import ssvv.example.repository.StudentXMLRepository;
import ssvv.example.repository.TemaXMLRepository;
import ssvv.example.service.Service;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.ValidationException;
import ssvv.example.validation.Validator;
import static org.junit.Assert.*;

public class TestAddGrade {

    private NotaXMLRepository notaXMLRepository;

    private TemaXMLRepository temaXMLRepository;

    private StudentXMLRepository studentXMLRepository;
    private Service service;

    @Before
    public void setUp() {
        Validator<Nota> notaValidator = new Validator<Nota>() {
            @Override
            public void validate(Nota nota) throws ValidationException {
                if (nota.getID().getObject1() == null || nota.getID().equals("")) {
                    throw new ValidationException("ID Student invalid! \n");
                }
                if (nota.getID().getObject2() == null || nota.getID().equals("")) {
                    throw new ValidationException("ID Tema invalid! \n");
                }
                if (nota.getNota() < 0 || nota.getNota() > 10) {
                    throw new ValidationException("Nota invalida! \n");
                }
                if (nota.getSaptamanaPredare() < 0) {
                    throw new ValidationException("Saptamana de predare invalida! \n");
                }
            }

        };
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

        Validator<Student> studentValidator = new Validator<Student>() {
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

        this.notaXMLRepository = new NotaXMLRepository(notaValidator, "testNota.xml");
        this.temaXMLRepository = new TemaXMLRepository(temaValidator, "testTema.xml");
        this.studentXMLRepository = new StudentXMLRepository(studentValidator, "testStudent.xml");
        service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);
    }

    @After
    public void tearDown(){notaXMLRepository.clearAllGrades();
        temaXMLRepository.clearAllAssignments();
        studentXMLRepository.clearAllStudents();}

    @Test
    public void testAddAssignment_deadlineAndStartlineInTheValidRange_assignmentSuccessfullyAdded(){
        assertEquals(1, service.saveTema("1", "descriere1", 2, 1));
        assertEquals(1, service.saveTema("2", "descriere2", 3, 2));
    }

    @Test
    public void testAddStudent_nameIsAlphabetical_addsStudentCorrectly(){
        assertEquals(1, service.saveStudent("1", "Bob", 211));
        assertEquals(1, service.saveStudent("2", "Maria", 211));
        assertEquals(1, service.saveStudent("3", "Ion", 211));
    }

    @Test
    public void testAddNota_GradeWithCorrectParams_GradeAddedSuccessfully(){
        int countStudents = 0;
        for (Student student : service.findAllStudents()) {
            countStudents++;
        }
        int countTeme = 0;
        for (Tema tema : service.findAllTeme()) {
            countTeme++;
        }
        if (countStudents == 0 && countTeme == 0){
            return;
        }
        assertEquals(1, service.saveNota("1", "1", 8, 3, "blana"));
        assertEquals(1, service.saveNota("2", "2", 8, 4, "blana"));
    }

    @Test
    public void testAll(){
        testAddAssignment_deadlineAndStartlineInTheValidRange_assignmentSuccessfullyAdded();
        testAddStudent_nameIsAlphabetical_addsStudentCorrectly();
        testAddNota_GradeWithCorrectParams_GradeAddedSuccessfully();
    }


}
