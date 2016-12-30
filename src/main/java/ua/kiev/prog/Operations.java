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
                System.out.println("Select operation: ");
                System.out.println("1.Add group");
                System.out.println("2.Add students");

                String choice = scanner.nextLine();
                switch (choice){
                    case "1":
                        addGroup();
                        break;
                    case "2":
                        selectGroup();
                        break;
                }
            }
        //}catch()
    }

    public void addGroup(){
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

    public void addStudent(){
        em.getTransaction().begin();
        try{
            Student student = new Student("vddsf","dgd");
            em.persist(student);
            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    public List<Group> selectGroup(){
        List<Group> groupList = null;
        try {
            Query query = em.createQuery("SELECT group FROM groups group",Group.class);
            groupList = (List<Group>) query.getResultList();
        }catch (NoResultException e){e.printStackTrace();}
        catch (NonUniqueResultException e){e.printStackTrace();}
        return groupList;
    }
}
