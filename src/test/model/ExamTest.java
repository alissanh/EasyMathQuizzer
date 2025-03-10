package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExamTest {
    private Exam exam;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private Equation equation1;
    private Equation equation2;
    private Equation equation3;
    private Equation equation4;



    @BeforeEach
    void setUp() {
        exam = new Exam();
        System.setOut(new PrintStream(outputStreamCaptor));

        equation1 = new Equation(10, "5 + 5");
        equation2 = new Equation(20, "10 + 10");
        equation3 = new Equation(4, "2 * 2");
        equation4 = new Equation(6, "3 - 1");

        exam.addEquation(equation1);
        exam.addEquation(equation2);
        exam.addEquation(equation3);
        exam.addEquation(equation4);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testConstructor() {
        exam = new Exam();
        assertEquals(0, exam.getEquationCount());
    }

    @Test
    void testAddEquationAndGetEquationCount() {
        assertEquals(4, exam.getEquationCount());
    }

    @Test
    void testGetEquations() {
        List<Equation> equations = exam.getEquations();

        assertEquals(4, equations.size());
        assertEquals(10, equations.get(0).getSolution());
        assertEquals(20, equations.get(1).getSolution());
        assertEquals(4, equations.get(2).getSolution());
        assertEquals(6, equations.get(3).getSolution());
    }

    @Test
    void testShuffleEquations() {
        assertEquals(equation1, exam.getEquations().get(0));
        exam.shuffleEquations();
        assertNotEquals(equation1, exam.getEquations().get(0));
    }

    @Test
    void testGetEqnByOp_Mult() {
        List<Equation> filteredEqn = exam.getEquationByOperator("+");
        assertEquals(equation1, filteredEqn.get(0));
        assertEquals(equation2, filteredEqn.get(1));
    }

    @Test
    void testGetEqnByOp_None() {
        List<Equation> filteredEqn = exam.getEquationByOperator("/");
        assertTrue(filteredEqn.isEmpty());
    }

    // !!!
    @Test
    void testClearExamFull() {
        assertEquals(4, exam.getEquationCount());

        exam.clearExam();
        assertEquals(0, exam.getEquationCount());

    }

    @Test
    void testClearExamEmpty() {
        Exam newExam = new Exam();
        newExam.clearExam();
        assertEquals(0, newExam.getEquationCount());
    }
}
