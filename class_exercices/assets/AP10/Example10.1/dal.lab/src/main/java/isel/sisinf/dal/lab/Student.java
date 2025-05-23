/*
MIT License

Copyright (c) 2022, Nuno Datia, João Vitorino, ISEL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package isel.sisinf.dal.lab;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
public class Student {
    // PROPERTIES
    @Id
    private int studentNumber;

    private String name;

    private Date dateBirth;

    private char sex;

    @ManyToOne
    @JoinColumn(name = "country")
    private Country country;


    // CONSTRUCTORS
    public Student() {}

    public Student(int stNumber, String name, java.util.Date dtBirth, char sex) {
        this.studentNumber = stNumber;
        this.name = name;
        this.dateBirth = new java.sql.Date(dtBirth.getTime());
        this.sex = sex;
    }

    // SETTERS and GETTERS
    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getCountry() { return country.getName(); }

    public void setCountry(Country country) { this.country = country; }

    // UTILS
    @Override
    public String toString() {
        return "Student [studentNumber=" + studentNumber + ", name=" + name + ", dateBirth=" + dateBirth + ", sex="
                + sex + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        return studentNumber == other.studentNumber;
    }
}
