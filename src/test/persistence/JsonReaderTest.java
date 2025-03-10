package persistence;

import model.Equation;
import model.Exam;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNoFile() {
        JsonReader reader = new JsonReader("./data/coolio.json");
        try {
            Exam exam = reader.read();
            fail("IO Exception expected");
        } catch (IOException e) {

        }

    }

    @Test
    void testReaderEmptyExam() {
        JsonReader reader = new JsonReader("./data/testEmptyExam.json");
        try {
            Exam exam = reader.read();
            assertEquals(0, exam.getEquationCount());
        } catch (IOException e) {
            fail("Should not have thrown exception. ");
        }
    }

    @Test
    void testReaderNormal() {
        JsonReader reader = new JsonReader("./data/testNormalExam.json");
        try {
            Exam exam = reader.read();
            assertEquals(5, exam.getEquationCount());
            List<Equation> equations = exam.getEquations();
            checkEquation("6 + 4", 10, equations.get(0));
            checkEquation("110 / 10", 10, equations.get(3));
        } catch (IOException e) {
            fail("Should not have thrown exception. ");
        }
    }
}
