package persistence;

import model.Equation;
import model.Exam;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterNoFile() {
        try {
            Exam exam = new Exam();
            JsonWriter writer = new JsonWriter("./data/I\1llegalname.json");
            writer.open();
            fail("Should have thrown exception");
        } catch (IOException e) {

        }
    }

    @Test
    void testWriterEmptyExam() {
        try {
            Exam exam = new Exam();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyExam.json");
            writer.open();
            writer.write(exam);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyExam.json");
            exam = reader.read();
            assertEquals(0,exam.getEquationCount());
        } catch (IOException e) {
            fail("Should not have thrown exception. ");
        }
    }

    @Test
    void testWriterNormalExam() {
        try {
            Exam exam = new Exam();
            Equation equation1 = new Equation(2, "1 + 1");
            Equation equation2 = new Equation(10, "100 / 10");
            Equation equation3 = new Equation(30, "5 * 6");

            exam.addEquation(equation1);
            exam.addEquation(equation2);
            exam.addEquation(equation3);

            JsonWriter writer = new JsonWriter("./data/testWriterEmptyExam.json");
            writer.open();
            writer.write(exam);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyExam.json");
            exam = reader.read();
            List<Equation> equations = exam.getEquations();
            assertEquals(3 ,exam.getEquationCount());
            checkEquation("100 / 10", 10, equations.get(1));
            checkEquation("5 * 6", 30, equations.get(2));
        } catch (IOException e) {
            fail("Should not have thrown exception. ");
        }
    }
}
