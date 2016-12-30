package ua.kiev.prog;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

/**
 * Created by User on 28.12.2016.
 */
public class Operations {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private Scanner scanner;

    public Operations() {
        emf = Persistence.createEntityManagerFactory("GroupsManager");
        em = emf.createEntityManager();
        scanner = new Scanner(System.in);
        operationSelection();
    }

    public void operationSelection(){
        //try{
            while(true) {
                System.out.println();
                System.out.println("Select operation: ");
                System.out.println("1.Add group");
                System.out.println("2.Choice groop");
                System.out.println("3.Quantity students in groups");

                String choice = scanner.nextLine();
                switch (choice){
                    case "1":
                        addGroup();
                        break;
                    case "2":
                        choiceGroup();
                        break;
                    case "3":
                        quantityStudentsInGroups();
                        break;
                }
            }
        //}catch()
    }

    public void addGroup(){
        System.out.println();
        System.out.print("Enter group's name: ");
        String name = scanner.nextLine();

        em.getTransaction().begin();
        try{
            Group group = new Group(name);
            em.persist(group);
            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    public void choiceGroup(){
        boolean flag = true;
        List<Group> groupList = null;
        while(flag) {
            try {
                Query query = em.createQuery("SELECT group FROM Group group", Group.class);
                groupList = (List<Group>) query.getResultList();
                for (Group g : groupList) {
                    System.out.println(g);
                }
                System.out.println();
                System.out.println("0.  Close");
                System.out.print("Choice the group: ");
                int check = Integer.parseInt(scanner.nextLine());
                if(check != 0){
                    groupOperation(groupList.get(check - 1));
                }else{
                    flag = false;
                }
            } catch (NoResultException e) {
                e.printStackTrace();
            } catch (NonUniqueResultException e) {
                e.printStackTrace();
            }
        }
    }

    public void groupOperation(Group group){
        boolean flag = true;
        while(flag) {
            System.out.println();
            System.out.println("Operations for group.");
            System.out.println("1. Add student");
            System.out.println("2. Delete student");
            System.out.println("3. Select list of student");
            System.out.println("0. Close");

            int check = Integer.parseInt(scanner.nextLine());

            switch (check) {
                case 1:
                    addStudent(group);
                    break;
                case 2:
                    delStudent(group);
                    break;
                case 3:
                    selectListOfStudents(group);
                    break;
                case 0:
                    flag = false;
                    break;
            }
        }
    }

    public void addStudent(Group group){
        System.out.println();
        System.out.print("Enter name of student: ");
        String name = scanner.nextLine();
        System.out.print("Enter surname: ");
        String surname = scanner.nextLine();
        em.getTransaction().begin();
        try {
            Student student = new Student(name, surname, group);
            em.persist(student);
            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    public void delStudent(Group group){
        int groupId = group.getId();
        List<Student> studentsList = null;
        System.out.println();
        em.getTransaction().begin();
        try {
            Query query = em.createQuery("SELECT student FROM Student student WHERE student.group.id = :groupId", Student.class);
            query.setParameter("groupId", groupId);
            studentsList = (List<Student>) query.getResultList();
            System.out.println("List of group " + group.getName());
            for (Student student : studentsList) {
                System.out.println(student);
            }
            int check = Integer.parseInt(scanner.nextLine());
            em.remove(studentsList.get(check - 1));
            em.getTransaction().commit();
        }catch(Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void selectListOfStudents(Group group){
        int groupId = group.getId();
        List<Student> studentsList = null;
        System.out.println();
        em.getTransaction().begin();
        try {
            Query query = em.createQuery("SELECT student FROM Student student WHERE student.group.id = :groupId", Student.class);
            query.setParameter("groupId", groupId);
            studentsList = (List<Student>) query.getResultList();
            System.out.println("List of group " + group.getName());
            for (Student student : studentsList) {
                System.out.println(student);
            }
            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void quantityStudentsInGroups(){
        System.out.println();
        Query query = em.createQuery("select groups.name, sCount from \n" +
                "(select groups.id as groupId, count(students.id ) as sCount from students LEFT JOIN groups \n" +
                "ON students.group_id = groups.id\n" +
                "group by group_id) as VmTable inner join groups on VmTable.groupId = groups .id");
    }
}
