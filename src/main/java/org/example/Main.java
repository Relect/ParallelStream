package org.example;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    static class Student {
        private String name;
        private Map<String, Integer> grades;
        public Student(String name, Map<String, Integer> grades) {
            this.name = name;
            this.grades = grades;
        }
        public Map<String, Integer> getGrades() {
            return grades;
        }
    }
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("Student1", Map.of("Math", 90, "Physics", 85)),
                new Student("Student2", Map.of("Math", 95, "Physics", 88)),
                new Student("Student3", Map.of("Math", 88, "Chemistry", 92)),
                new Student("Student4", Map.of("Physics", 78, "Chemistry", 85))
        );

        Map<String , List<Integer>> map = students.parallelStream()
                .map(Student::getGrades)
                .flatMap(item  -> item.entrySet().stream())
                .collect(Collectors.groupingByConcurrent(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        var mapResult = map.entrySet().parallelStream()
                        .map( entry -> {
                            double average = 0.0;
                            var list = entry.getValue();
                            for (Integer integer : list) {
                                average += integer;
                            }
                            average = average / list.size();
                            return new AbstractMap.SimpleEntry<>(entry.getKey(), average);
                        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        mapResult.forEach( (k,v) -> System.out.println(k + ":" + v));
    }
}
