package model;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class EquationTest {
    private Equation equation;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Test
    public void testConstructor() {
        equation = new Equation(35, "30 + 5");
        assertEquals(35, equation.getSolution());
    }

    @Test
    public void testEvaluate_Addition() {
        try {
            equation = new Equation(10, "1 + 11");
            assertEquals(12, equation.evaluate());
        } catch (InvalidEquationException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testEvaluate_Subtraction() {
        try {
            equation = new Equation(5, "40 - 5");
            assertEquals(35, equation.evaluate());
        } catch (InvalidEquationException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testEvaluate_Multiplication() {
        try {
            equation = new Equation(15, "27 * 11");
            assertEquals(297, equation.evaluate());
        } catch (InvalidEquationException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testEvaluate_Division() {
        try {
            equation = new Equation(2, "12 / 4");
            assertEquals(3, equation.evaluate());
        } catch (InvalidEquationException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testEvaluate_DivisionByZero() {
        try {
            equation = new Equation(0, "11 / 0");
            equation.evaluate();
        } catch (InvalidEquationException e) {
            assertEquals("/", equation.getOperator());
        }
    }

    @Test
    public void testEvaluate_NotValidOperator() {
        try {
            equation = new Equation(0, "11 ^ 0");
            equation.evaluate();
        } catch (InvalidEquationException e) {

        }
    }

    @Test
    public void testGetOperator_ValidInput() {
        try {
            Equation equation = new Equation(0, "5 * 1");
            assertEquals("*", equation.getOperator());
        } catch (Exception e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testGetOperator_InvalidInput() {
        try {
            equation = new Equation(0, "invalid");
            assertEquals("", equation.getOperator());
        } catch (Exception e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testParseUserAnswerValidCorrect() {
        try {
            equation = new Equation(3, "6 / 2");
            equation.parseUserAnswer("3");
        } catch (InvalidInputException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testParseUserAnswerValidIncorrect() {
        try {
            equation = new Equation(3, "6 / 2");
            equation.parseUserAnswer("5");
        } catch (InvalidInputException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testParseUserAnswerInvalid() {
        try {
            equation = new Equation(3, "6 / 2");
            equation.parseUserAnswer("Cool");
        } catch (InvalidInputException e) {

        }
    }

    @Test
    public void testDisplay() {
        try {
            System.setOut(new PrintStream(outputStreamCaptor));
            equation = new Equation(3, "6 / 2");
            equation.display();
            String output = outputStreamCaptor.toString().trim();
            assertEquals(output, "Equation: 6 / 2");
        } catch (Exception e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testDisplayInvalid() {
        try {
            System.setOut(new PrintStream(outputStreamCaptor));
            equation = new Equation(3, "Coolio");
            equation.display();
            String output = outputStreamCaptor.toString().trim();
            assertEquals(output, "Equation: Coolio");
        } catch (Exception e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testCheckAnswerValid() {
        try {
            equation = new Equation(5, "1 + 4");
            String data = "5";
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            boolean result = equation.checkAnswer();
            assertTrue(result);
        } catch (InvalidEquationException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testCheckAnswerValidWrong() {
        try {
            equation = new Equation(5, "1 + 4");
            String data = "10";
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            boolean result = equation.checkAnswer();
            assertFalse(result);
        } catch (InvalidEquationException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testCheckAnswerInvalid() {
        try {
            equation = new Equation(5, "1 0 4");
            String data = "5";
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            equation.checkAnswer();
        } catch (InvalidEquationException e) {

        }
    }

    @Test
    public void testCheckAnswerInvalidInput() {
        try {
            System.setOut(new PrintStream(outputStreamCaptor));
            equation = new Equation(5, "1 + 4");
            String data = "Five";
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            equation.checkAnswer();
            String output = outputStreamCaptor.toString().trim();
            assertTrue(output.contains("Invalid input"));
        } catch (InvalidEquationException e) {
            fail("Should not have thrown exception. ");
        }
    }

    @Test
    public void testParseEquationValidAdd() {
        try {
            equation = Equation.parseEquation("600 + 60");
            assertEquals("+", equation.getOperator());
            assertEquals(660, equation.getSolution());
        } catch (InvalidEquationException | InvalidInputException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testParseEquationValidSubstract() {
        try {
            equation = Equation.parseEquation("600 - 60");
            assertEquals("-", equation.getOperator());
            assertEquals(540, equation.getSolution());
        } catch (InvalidEquationException | InvalidInputException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testParseEquationValidMult() {
        try {
            equation = Equation.parseEquation("600 * 60");
            assertEquals("*", equation.getOperator());
            assertEquals(36000, equation.getSolution());
        } catch (InvalidEquationException | InvalidInputException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testParseEquationValidDivide() {
        try {
            equation = Equation.parseEquation("600 / 60");
            assertEquals("/", equation.getOperator());
            assertEquals(10, equation.getSolution());
        } catch (InvalidEquationException | InvalidInputException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testParseEquationInvalidInput() {
        try {
            equation = Equation.parseEquation("Coolio");
        } catch (InvalidEquationException | InvalidInputException e) {
        }
    }

    @Test
    public void testParseEquationInvalidEquation() {
        try {
            equation = Equation.parseEquation("4 + 10 + 15");
        } catch (InvalidInputException e) {
            fail("Should not have thrown exception");

        } catch (InvalidEquationException e) {
            assertNull(equation);
        }
    }

    @Test
    public void testParseEquationInvalidEquationZero() {
        try {
            equation = Equation.parseEquation("4 / 0");
        } catch (InvalidInputException e) {
            fail("Should not have thrown exception");

        } catch (InvalidEquationException e) {
            assertNull(equation);
        }
    }

    @Test
    public void testParseEquationInvalidOperator() {
        try {
            equation = Equation.parseEquation("4 ^ 0");
        } catch (InvalidInputException e) {
            fail("Should not have thrown exception");

        } catch (InvalidEquationException e) {

        }
    }

    @Test
    public void testParseNumberValid() {
        try {
            int num = Equation.parseNumber("5");
            assertEquals(5, num);
        } catch (InvalidInputException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testParseNumberInvalid() {
        try {
            Equation.parseNumber("five");
        } catch (InvalidInputException e) {

        }
    }


}