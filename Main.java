import java.io.*;
import java.util.*;

class FaceBook implements Serializable {
    private HashMap<String, User> user_details = new HashMap<>();
    public ArrayList<String> Username = new ArrayList<>();
    public ArrayList<String> phonenumber = new ArrayList<>();

    public HashMap<String, User> getUser_details() {
        return user_details;
    }

    public void setUser_details(HashMap<String, User> user_details) {
        this.user_details = user_details;
    }

    public static boolean autenticate(String a, String b, HashMap<String, User> user) {
        if (user.containsKey(a)) {
            String pass = user.get(a).getPassword();
            if (b.equals(pass)) {
                return true;
            } else {
                System.err.println("WRONG Password....!!!");
                return false;
            }
        }
        System.err.println("User phone number not found....!!!");
        return false;

    }

    public void sendFriendRequest(User fuser, User cuser) {
        ArrayList<String> user = fuser.getPendingList();
        user.add(cuser.getName());
        fuser.setPendingList(user);
    }

    public String encrypt(String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] >= 'a' && c[i] <= 'z') {
                if (c[i] != 'z') {
                    c[i] = (char) (c[i] + 1);
                } else
                    c[i] = 'a';
            } else if (c[i] >= '1' && c[i] <= '9') {
                if (c[i] != '9') {
                    c[i] = (char) (c[i] + 1);
                } else
                    c[i] = '0';
            } else if (c[i] >= 'A' && c[i] <= 'Z') {
                if (c[i] != 'Z') {
                    c[i] = (char) (c[i] + 1);
                } else
                    c[i] = 'A';
            }
        }
        return new String(c);
    }

    public String decrypt(String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] >= 'a' && c[i] <= 'z') {
                if (c[i] != 'a') {
                    c[i] = (char) (c[i] - 1);
                } else
                    c[i] = 'z';
            } else if (c[i] >= '1' && c[i] <= '9') {
                if (c[i] != '0') {
                    c[i] = (char) (c[i] - 1);
                } else
                    c[i] = '9';
            } else if (c[i] >= 'A' && c[i] <= 'Z') {
                if (c[i] != 'A') {
                    c[i] = (char) (c[i] - 1);
                } else
                    c[i] = 'Z';
            }
        }
        return new String(c);
    }

}

class User implements Serializable {
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Password;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String phone_number;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    private ArrayList<String> friendList = new ArrayList<>();
    private ArrayList<String> pendingList = new ArrayList<>();

    public ArrayList<String> getPendingList() {
        return pendingList;
    }

    public void setPendingList(ArrayList<String> pendingList) {
        this.pendingList = pendingList;
    }

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    User(String name, String pass, String phone_number) {
        this.Name = name;
        this.Password = pass;
        this.phone_number = phone_number;
    }

}

class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("FaceBookDB.txt");
        FaceBook fb = new FaceBook();
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                FaceBook read = (FaceBook) ois.readObject();
                HashMap<String, User> custom = read.getUser_details();
                fb.setUser_details(custom);
                fb.Username = read.Username;
                fb.phonenumber = read.phonenumber;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        File Ufile = new File("Userdetails.txt");
        try {
            if (!Ufile.exists()) {
                Ufile.createNewFile();
            }
            // else {
            // FileInputStream fis = new FileInputStream(Ufile);
            // ObjectInputStream ois = new ObjectInputStream(fis);
            // User read = (User) ois.readObject();
            // ArrayList<String> custom = read.getFriendList();
            // custom.setFriendList(custom);
            // }
        } catch (Exception e) {
            System.out.println(e);
        }
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("1.login \n2.register \n3.exit");
            System.out.println("-------------------------------------------------------------------------");
            int n = sc.nextInt();
            sc.nextLine();
            switch (n) {
                case 1: {
                    System.out.println("-------------------------------------------------------------------------");
                    System.out.println("User Login");
                    System.out.println("-------------------------------------------------------------------------");
                    System.out.print("Enter the phoneNumber : ");
                    String phoneNumber = sc.nextLine();
                    System.out.print("Enter password : ");
                    String pass = sc.nextLine();
                    boolean loop1 = true;
                    while (loop) {
                        if (fb.autenticate(phoneNumber, pass, fb.getUser_details())) {
                            User currentuser = fb.getUser_details().get(phoneNumber);
                            System.out.println("---------------------------Welcome " + currentuser.getName()
                                    + "----------------------------------------------");
                            System.out.println(
                                    "1.Send Friend Request \n2.Show Friend List \n3.Pending Request \n4.remove friend \n5.Search \n6.edit profile \n7.LogOut");
                            System.out.println(
                                    "-------------------------------------------------------------------------");
                            int k = sc.nextInt();
                            sc.nextLine();
                            switch (k) {
                                case 1: {
                                    System.out.println("Enter the name : ");
                                    String fname = sc.nextLine();
                                    if (fb.Username.contains(fname)) {
                                        String friendphno = fb.phonenumber.get(fb.Username.indexOf(fname));
                                        System.out.println("click ENTER to send request");
                                        if (sc.nextLine().length() == 0) {
                                            User frienduser = fb.getUser_details().get(friendphno);
                                            fb.sendFriendRequest(frienduser, currentuser);
                                            System.out.println("friend Request sended succesfully");
                                        } else {
                                            break;
                                        }
                                        break;
                                    }

                                }
                                case 2: {
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("                  " + currentuser.getName() + " Friend List");
                                    System.out.println("-------------------------------------------------------");
                                    int j = 1;
                                    for (String i : currentuser.getFriendList()) {
                                        System.out.println(String.valueOf(j) + "." + i);
                                        j += 1;
                                    }
                                    break;
                                }
                                case 3: {
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("                  " + currentuser.getName() + " Pending List");
                                    System.out.println("-------------------------------------------------------");
                                    int j = 1;

                                    for (String i : currentuser.getPendingList()) {
                                        System.out.println(String.valueOf(j) + "." + i);
                                        j += 1;
                                    }
                                    System.out.println(
                                            "-------------------------------------------------------------------------");
                                    while (true) {
                                        System.out.println("1.accept request \n2.exit");
                                        int p = sc.nextInt();
                                        if (p != 1)
                                            break;
                                        System.out.println("Enter number to Accept ");
                                        int s = sc.nextInt();
                                        if (s < j) {
                                            String ph = fb.phonenumber
                                                    .get(fb.Username.indexOf(currentuser.getPendingList().get(s - 1)));
                                            User friend = fb.getUser_details().get(ph);
                                            friend.getFriendList().add(currentuser.getName());
                                            currentuser.getFriendList().add(currentuser.getPendingList().get(s - 1));
                                            currentuser.getPendingList()
                                                    .remove(currentuser.getPendingList().get(s - 1));
                                            j -= 1;
                                        }
                                    }

                                    break;
                                }
                                case 4: {
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("                  " + currentuser.getName() + " Friend List");
                                    System.out.println("-------------------------------------------------------");
                                    int j = 1;
                                    for (String i : currentuser.getFriendList()) {
                                        System.out.println(String.valueOf(j) + "." + i);
                                        j += 1;
                                    }
                                    System.out.println("Enter the number to remove friend");
                                    int s = sc.nextInt();
                                    currentuser.getFriendList().remove(currentuser.getPendingList().get(s - 1));

                                    break;
                                }
                                case 5: {
                                    System.out.println("Enter the name to Search ");
                                    String s = sc.nextLine();
                                    if (fb.Username.contains(s)) {
                                        String ph = fb.phonenumber.get(fb.Username.indexOf(s));
                                        User friend = fb.getUser_details().get(ph);
                                        System.out.println("You and" + friend.getName() + " have mutuals friend ");
                                        ArrayList<String> flist = currentuser.getFriendList();
                                        for (String f : friend.getFriendList()) {
                                            if (flist.contains(f)) {
                                                System.out.println(f);
                                            }
                                        }
                                    } else {
                                        System.out.println("no user found");
                                    }
                                    break;
                                }
                                case 6: {
                                    System.out.println("what to edit ?\n1.name \n2.password");
                                    int y = sc.nextInt();
                                    sc.nextLine();
                                    if (y == 1) {
                                        System.out.println("enter name to edit : ");
                                        String newname = sc.nextLine();
                                        System.out.println("enter current password to conform : ");
                                        String currentpw = sc.nextLine();
                                        if (currentpw.equals(currentuser.getPassword())) {
                                            currentuser.setName(newname);
                                        } else {
                                            System.out.println("password Mismatch");
                                        }
                                    }
                                    if (y == 2) {
                                        System.out.println("Enter Password to Edit : ");
                                        String pw = sc.nextLine();
                                        System.out.println("enter current password to conform : ");
                                        String currentpw = sc.nextLine();
                                        if (currentpw.equals(currentuser.getPassword())) {
                                            currentuser.setPassword(pw);
                                        }
                                    }
                                    break;
                                }
                                case 7: {
                                    loop1 = false;
                                    break;
                                }

                            }
                        } else {
                            System.out.println("user login Invalid");
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.println("-------------------------------------------------------------------------");
                    System.out.println("User Registration");
                    System.out.println("-------------------------------------------------------------------------");
                    System.out.print("Enter the Username : ");
                    String username = sc.nextLine();
                    System.out.print("type password : ");
                    String userpass = sc.nextLine();
                    System.out.print("enter phonenumber : ");
                    String phoneno = sc.nextLine();
                    User u = new User(username, userpass, phoneno);
                    fb.Username.add(username);
                    fb.phonenumber.add(phoneno);
                    HashMap<String, User> hm = fb.getUser_details();
                    hm.put(phoneno, u);
                    fb.setUser_details(hm);
                    System.out.println("----------SUCCESSFULLY REGISERED----------");
                    break;
                }
                case 3: {
                    loop = false;
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(fb);
                    oos.flush();
                    break;
                }
            }
        }
    }
}