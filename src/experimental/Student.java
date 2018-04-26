package experimental;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return grade == student.grade &&
                Objects.equals(name, student.name);
    }
}
