package application.service;

import application.entity.Level;
import application.entity.Subject;

import java.util.LinkedHashSet;


public interface SystemService {

    LinkedHashSet<Level> getSystemLevels();
    LinkedHashSet<Subject> getSystemSubjects();
}
