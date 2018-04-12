package optimizations.optimizations_filter_map;

import datatypes.FStream;
import experimental.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

public class OriginalFStream extends MasterBenchmarkFilterMap{
    public static void main(String[] args) {
        OriginalFStream ofs = new OriginalFStream();

        ofs.populate();

        ofs.warmUp();

        ofs.measure();

        ofs.end();
    }

    @Override
    public ArrayList<Student> work() {
        Predicate<Student> p = s -> s.grade >= 95;
        Function<Student, Student> f = s -> new Student(s.name, Math.round((double) s.grade / 10));


        ArrayList<Student> res = (ArrayList<Student>) FStream.fstream(l)
                .filterfs(p)
                .mapfs(f)
                .unfstream();


        System.out.println(res.size());
        return res;
    }
}
