package experimental;

public class Student {
    public String name;
    public long grade;

    public Student(String name, long grade) {
        this.name = name;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "(" + name + ", " + grade + ')';
    }
}
