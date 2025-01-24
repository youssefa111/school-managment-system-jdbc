package utils.authorization;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import utils.CurrentSession;
import utils.constants.Role;

@Aspect
public class SecurityAspect {


    // Pointcut for methods annotated with @IsAdmin
    @Pointcut("@annotation(IsAdmin)")
    public void adminMethod() {}

    // Pointcut for methods annotated with @IsStudent
    @Pointcut("@annotation(IsStudent)")
    public void studentMethod() {}

    // Pointcut for methods annotated with @IsTeacher
    @Pointcut("@annotation(IsTeacher)")
    public void teacherMethod() {}

    // Advice for @IsAdmin methods
    @Around("adminMethod()")
    public Object checkAdminRole(ProceedingJoinPoint joinPoint) throws Throwable {
        Role currentRole = CurrentSession.getInstance().getCurrentRole();
        boolean isAuthenticated = CurrentSession.getInstance().isAuthenticated();

        if ( isAuthenticated == Boolean.FALSE || currentRole!= Role.ADMIN) {
            throw new SecurityException("Access denied: Requires Authenticated ADMIN role.");
        }
        return joinPoint.proceed(); // Proceed with the method execution if authorized
    }

    // Advice for @IsStudent methods
    @Around("studentMethod()")
    public Object checkStudentRole(ProceedingJoinPoint joinPoint) throws Throwable {
        Role currentRole = CurrentSession.getInstance().getCurrentRole();
        boolean isAuthenticated = CurrentSession.getInstance().isAuthenticated();
        if ( isAuthenticated == Boolean.FALSE || currentRole!= Role.STUDENT) {
            throw new SecurityException("Access denied: Requires Authenticated STUDENT role.");
        }
        return joinPoint.proceed(); // Proceed with the method execution if authorized
    }

    // Advice for @IsTeacher methods
    @Around("teacherMethod()")
    public Object checkTeacherRole(ProceedingJoinPoint joinPoint) throws Throwable {
        Role currentRole = CurrentSession.getInstance().getCurrentRole();
        boolean isAuthenticated = CurrentSession.getInstance().isAuthenticated();

        if (isAuthenticated == Boolean.FALSE || currentRole != Role.TEACHER) {
            throw new SecurityException("Access denied: Requires Authenticated TEACHER role.");
        }
        return joinPoint.proceed(); // Proceed with the method execution if authorized
    }
}
