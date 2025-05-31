package br.com.dio.model;

public class Space {

    private Integer actual;
    private final int expected;
    private final boolean fixed;

    public Space(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        this.actual = fixed ? expected : null;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer value) {
        if (fixed || value == null) return; // Pode explicar melhor isso? para que serve?
        actual = value;
    }


    public void clearSpace() {
        if (!fixed) actual = null; // Como assim? para que esse !fixed?
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }
}
