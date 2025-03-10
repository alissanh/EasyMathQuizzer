package persistence;

import model.Equation;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkEquation(String expression, int solution, Equation equation) {
        assertEquals(solution, equation.getSolution());
        assertEquals(expression, equation.getExpression());
    }
}
