import java.util.HashMap;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.sql.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class WrongPasswordException extends Exception {
    WrongPasswordException(String s) {
        super(s);
    }
}

class ReadFileError extends Exception {
    ReadFileError(String s) {
        super(s);
    }
}

class IsNotAFile extends Exception {
    IsNotAFile(String s) {
        super(s);
    }
}

class Signup {
    Scanner sc = new Scanner(System.in);
    int userId = 0;

    // THE BELOW METHOD ALLOWS USER TO SIGN-UP INTO THE SYSTEM FOR THE FIRST TIME.
    void initial_Signup1(HashMap<Integer, String> hmdirec) throws Exception {
        BufferedReader readerDirec = new BufferedReader(new FileReader("recordDirec.txt"));
        String s12;
        while ((s12 = readerDirec.readLine()) != null) {
            String arr[] = s12.split(" ");
            hmdirec.put(Integer.parseInt(arr[0]), arr[1]);
        }
        readerDirec.close();
        Class.forName("com.mysql.cj.jdbc.Driver");
        String dburl = "jdbc:mysql://localhost:3306/project_java";
        String dbuser = "root";
        String dbpass = "";
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
        /*
         * if(con != null) {
         * System.out.println("CONNECTED");
         * } else {
         * System.out.println("NOT CONNECT");
         * }
         */
        BufferedReader userReader = new BufferedReader(new FileReader("record.txt"));
        String s = "";
        while ((s = userReader.readLine()) != null) {
            String sarray[] = s.split(" ");
            userId = Integer.parseInt(sarray[0]);
        }
        userReader.close();
        userId = userId + 1;
        FileWriter fw = new FileWriter("record.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        FileWriter fwdirec = new FileWriter("recordDirec.txt", true);
        BufferedWriter bwdirec = new BufferedWriter(fwdirec);
        bw.write("" + userId);
        bw.newLine();
        bw.close();
        int initial_try = 3;
        int initial_pass = 1234;
        boolean checkPass = true;
        boolean checkCatch = false;
        System.out.println("Your user id : " + userId);
        System.out.println("Your initial password : " + initial_pass);
        while (checkPass) {
            if (initial_try == 0) {
                throw new WrongPasswordException("--ALL ATTEMPTS OVER--ENTRY FAILED--");
            }
            int useridCheck, passCheck;
            while (true) {
                try {
                    if (checkCatch) {
                        sc.nextLine();
                    }
                    System.out.println("ENTER USERID : ");
                    useridCheck = sc.nextInt();
                    System.out.println("ENTER PASSWORD : ");
                    passCheck = sc.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("Enter only integer in id and password field here!");
                    checkCatch = true;
                }
            }
            if (passCheck == initial_pass && useridCheck == userId) {
                File makedirec = new File("user_" + userId);
                if (!makedirec.isDirectory()) {
                    makedirec.mkdir();
                }
                hmdirec.put(userId, "user_" + userId);
                bwdirec.write("" + userId + " " + "user_" + userId);
                bwdirec.newLine();
                checkPass = false;
                bwdirec.flush();
            } else {
                initial_try--;
                System.out.println("--INCORRECT--");
                System.out.println("Tries Remaning : " + initial_try);
                System.out.println("--TRY AGAIN--");
            }
        }
        sc.nextLine();
        while (true) {
            System.out.println("Do you want to change password : ");
            System.out.println("(Enter 'yes' to change the password)");
            String ansChangePass = sc.nextLine();
            if (ansChangePass.equalsIgnoreCase("YES")) {
                boolean checkCatchPass = false;
                while (true) {
                    System.out.println("ENTER NEW PASSWORD : ");
                    try {
                        if (checkCatchPass) {
                            sc.nextLine();
                        }
                        initial_pass = sc.nextInt();
                        break;
                    } catch (Exception e) {
                        System.out.println("ENTER ONLY INTEGERS!");
                        checkCatchPass = true;
                    }
                }
                System.out.println("PASSWORD SUCCESSFULLY UPDATED : ");
                System.out.println("--ENTRY SUCCESSFULL--");
                break;
            } else if (ansChangePass.equalsIgnoreCase("NO")) {
                System.out.println("--ENTRY SUCCESSFULL--");
                break;
            } else {
                System.out.println("Enter yes or no only!");
            }
        }
        String sql = "insert into datastore values (?,?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, userId);
        pst.setInt(2, initial_pass);
        pst.execute();
    }
}

class Login {
    int userfinal = 0;
    Scanner sc = new Scanner(System.in);

    // THE BELOW FUNCTION WILL ALLOW USER THAT HAS ALREADY SIGNED UP INTO THE SYSTEM
    // ONCE, TO LOGIN INTO THE SYSTEM AGAIN USING THEIR RESPECTIVE ID AND PASSWORD
    void initial_Login1() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String dburl = "jdbc:mysql://localhost:3306/project_java";
        String dbuser = "root";
        String dbpass = "";
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
        /*
         * if (con != null) {
         * System.out.println("COONNECTED");
         * } else {
         * System.out.println("NOT CONNECT");
         * }
         */
        int attemptsSignin = 3;
        boolean checkPass = true;
        while (checkPass) {
            String sql = "select * from datastore";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (attemptsSignin == 0) {
                throw new WrongPasswordException("--ALL ATTEMPTS OVER--ENTRY FAILED--");
            }
            boolean checkCatch = false;
            int useridCheck, passCheck;
            while (true) {
                try {
                    if (checkCatch) {
                        sc.nextLine();
                    }
                    System.out.println("ENTER USERID : ");
                    useridCheck = sc.nextInt();
                    System.out.println("ENTER PASSWORD : ");
                    passCheck = sc.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("Enter only integer in id and password field here!");
                    checkCatch = true;
                }
            }
            while (rs.next()) {
                if (rs.getInt(1) == (useridCheck) && rs.getInt(2) == passCheck) {
                    checkPass = false;
                    userfinal = useridCheck;
                    System.out.println("LOGIN SUCCESSFULL");
                    break;
                }
            }
            if (checkPass == true) {
                System.out.println("--ENTER VALID ID AND PASSWORD--");
                attemptsSignin--;
            }
        }
    }
}

class Encrypter {
    // THE BELOW METHOD WILL TAKE A STRING AS INPUT, THEN ENCRYPT IT AS PER OUR
    // SYSTEM AND THEN RETURN THE ENCRYPTED STRING.
    static String encrypt(String str1) {
        String str = "";
        String Newstr = "";
        str = str1;
        for (int i = 0; i < str.length(); i++) {
            char ch = Character.toLowerCase(str.charAt(i));
            switch (ch) {
                case 'a':
                    Newstr = Newstr + "{";
                    break;
                case 'b':
                    Newstr = Newstr + "}";
                    break;
                case 'c':
                    Newstr = Newstr + "#";
                    break;
                case 'd':
                    Newstr = Newstr + "~";
                    break;
                case 'e':
                    Newstr = Newstr + "+";
                    break;
                case 'f':
                    Newstr = Newstr + "-";
                    break;
                case 'g':
                    Newstr = Newstr + "*";
                    break;
                case 'h':
                    Newstr = Newstr + "@";
                    break;
                case 'i':
                    Newstr = Newstr + "/";
                    break;
                case 'j':
                    Newstr = Newstr + "5";
                    break;
                case 'k':
                    Newstr = Newstr + "?";
                    break;
                case 'l':
                    Newstr = Newstr + "$";
                    break;
                case 'm':
                    Newstr = Newstr + "!";
                    break;
                case 'n':
                    Newstr = Newstr + "^";
                    break;
                case 'o':
                    Newstr = Newstr + "(";
                    break;
                case 'p':
                    Newstr = Newstr + ")";
                    break;
                case 'q':
                    Newstr = Newstr + "<";
                    break;
                case 'r':
                    Newstr = Newstr + ">";
                    break;
                case 's':
                    Newstr = Newstr + "=";
                    break;
                case 't':
                    Newstr = Newstr + ";";
                    break;
                case 'u':
                    Newstr = Newstr + ",";
                    break;
                case 'v':
                    Newstr = Newstr + "_";
                    break;
                case 'w':
                    Newstr = Newstr + "[";
                    break;
                case 'x':
                    Newstr = Newstr + "]";
                    break;
                case 'y':
                    Newstr = Newstr + ":";
                    break;
                case 'z':
                    Newstr = Newstr + "`";
                    break;
                case ' ':
                    Newstr = Newstr + " ";
                    break;
                case '.':
                    Newstr = Newstr + '3';
                    break;
                case ',':
                    Newstr = Newstr + "1";
                    break;
                case '(':
                    Newstr = Newstr + '4';
                    break;
                case ')':
                    Newstr = Newstr + "7";
                    break;
                case '?':
                    Newstr = Newstr + "2";
                    break;
                case '!':
                    Newstr = Newstr + "8";
                    break;
                case '-':
                    Newstr = Newstr + "6";
                    break;
                case '%':
                    Newstr = Newstr + "9";
                    break;
                case '1':
                    Newstr = Newstr + "r";
                    break;
                case '2':
                    Newstr = Newstr + "k";
                    break;
                case '3':
                    Newstr = Newstr + "b";
                    break;
                case '4':
                    Newstr = Newstr + "e";
                    break;
                case '5':
                    Newstr = Newstr + "q";
                    break;
                case '6':
                    Newstr = Newstr + "h";
                    break;
                case '7':
                    Newstr = Newstr + "u";
                    break;
                case '8':
                    Newstr = Newstr + "y";
                    break;
                case '9':
                    Newstr = Newstr + "w";
                    break;
                case '0':
                    Newstr = Newstr + "z";
                    break;
            }
        }
        return Newstr;
    }
}

class Decrypter {
    // THE BELOW METHOD WILL TAKE A STRING AS INPUT, THEN DECRYPT IT AS PER OUR
    // SYSTEM AND THEN RETURN THE DECRYPTED STRING.
    static String decrypt(String str1) {
        String str = "";
        String Newstr = "";
        str = str1;
        for (int i = 0; i < str.length(); i++) {
            char ch = Character.toLowerCase(str.charAt(i));
            switch (ch) {
                case '{':
                    Newstr = Newstr + "a";
                    break;
                case '}':
                    Newstr = Newstr + "b";
                    break;
                case '#':
                    Newstr = Newstr + "c";
                    break;
                case '~':
                    Newstr = Newstr + "d";
                    break;
                case '+':
                    Newstr = Newstr + "e";
                    break;
                case '-':
                    Newstr = Newstr + "f";
                    break;
                case '*':
                    Newstr = Newstr + "g";
                    break;
                case '@':
                    Newstr = Newstr + "h";
                    break;
                case '/':
                    Newstr = Newstr + "i";
                    break;
                case '5':
                    Newstr = Newstr + "j";
                    break;
                case '?':
                    Newstr = Newstr + "k";
                    break;
                case '$':
                    Newstr = Newstr + "l";
                    break;
                case '!':
                    Newstr = Newstr + "m";
                    break;
                case '^':
                    Newstr = Newstr + "n";
                    break;
                case '(':
                    Newstr = Newstr + "o";
                    break;
                case ')':
                    Newstr = Newstr + "p";
                    break;
                case '<':
                    Newstr = Newstr + "q";
                    break;
                case '>':
                    Newstr = Newstr + "r";
                    break;
                case '=':
                    Newstr = Newstr + "s";
                    break;
                case ';':
                    Newstr = Newstr + "t";
                    break;
                case ',':
                    Newstr = Newstr + "u";
                    break;
                case '_':
                    Newstr = Newstr + "v";
                    break;
                case '[':
                    Newstr = Newstr + "w";
                    break;
                case ']':
                    Newstr = Newstr + "x";
                    break;
                case ':':
                    Newstr = Newstr + "y";
                    break;
                case '`':
                    Newstr = Newstr + "z";
                    break;
                case ' ':
                    Newstr = Newstr + " ";
                    break;
                case '3':
                    Newstr = Newstr + '.';
                    break;
                case '1':
                    Newstr = Newstr + ",";
                    break;
                case '4':
                    Newstr = Newstr + '(';
                    break;
                case '7':
                    Newstr = Newstr + ")";
                    break;
                case '2':
                    Newstr = Newstr + "?";
                    break;
                case '8':
                    Newstr = Newstr + "!";
                    break;
                case '6':
                    Newstr = Newstr + "-";
                    break;
                case '9':
                    Newstr = Newstr + "%";
                    break;
                case 'r':
                    Newstr = Newstr + "1";
                    break;
                case 'k':
                    Newstr = Newstr + "2";
                    break;
                case 'b':
                    Newstr = Newstr + "3";
                    break;
                case 'e':
                    Newstr = Newstr + "4";
                    break;
                case 'q':
                    Newstr = Newstr + "5";
                    break;
                case 'h':
                    Newstr = Newstr + "6";
                    break;
                case 'u':
                    Newstr = Newstr + "7";
                    break;
                case 'y':
                    Newstr = Newstr + "8";
                    break;
                case 'w':
                    Newstr = Newstr + "9";
                    break;
                case 'z':
                    Newstr = Newstr + "0";
                    break;
            }
        }
        return Newstr;
    }
}

class Content {
    // THE BELOW METHOD WILL PROMPT USER TO ADD A FILE IN WHICH HE WANT TO STORE HIS
    // ENCRYPTED DATA AND ALSO ASK USER FOR A PASSWORD FOR THAT RESPECTIVE FILE.
    void addFile(HashMap<String, String> hm, HashMap<Integer, String> hmdirec, Signup s, Login l) throws Exception {
        Scanner sc = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader("recordDirec.txt"));
        String s12;
        while ((s12 = br.readLine()) != null) {
            String arr[] = s12.split(" ");
            hmdirec.put(Integer.parseInt(arr[0]), arr[1]);
        }
        String file;
        String regex = "^.+\\.txt$";
        Pattern pattern = Pattern.compile(regex);
        while (true) {
            System.out.println("ENTER FILE THAT YOU WANT TO ENTER DATA INTO : ");
            file = sc.nextLine();
            Matcher matcher = pattern.matcher(file);
            if (matcher.matches()) {
                break;
            } else {
                System.out.println("Enter a valid file name where it ends with '.txt'!");
            }
        }
        if (s == null) {
            File f = new File(hmdirec.get(l.userfinal) + "//" + file);
            f.createNewFile();
        } else {
            File f = new File(hmdirec.get(s.userId) + "//" + file);
            f.createNewFile();
        }
        System.out.println("Enter a password for file to be encrypted: ");
        String key = Encrypter.encrypt(sc.nextLine());
        if (s == null) {
            hm.put(hmdirec.get(l.userfinal) + "//" + file, key);
        } else {
            hm.put(hmdirec.get(s.userId) + "//" + file, key);
        }
        FileWriter fw = new FileWriter("recordHash.txt", true);
        BufferedWriter bw2 = new BufferedWriter(fw);
        if (s == null) {
            bw2.write(hmdirec.get(l.userfinal) + "//" + file + " " + key);
            bw2.newLine();
        } else {
            bw2.write(hmdirec.get(s.userId) + "//" + file + " " + key);
            bw2.newLine();
        }
        System.out.println(hm);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        System.out.println("Write content that you want to encrypt and then add to the file : ");
        String fileEntry = sc.nextLine();
        bw.write(Encrypter.encrypt(fileEntry));
        bw.close();
        bw2.close();
    }

    // THE BELOW METHOD WILL PROMPT USER TO READ THE DATA THAT THEY HAVE ENTERED
    // INTO THE FILE THAT WAS ENCRYPTED ONLY IF THEY ENTER THE CORRECT PASSWORD FOR
    // THAT PARTICULAR FILE.
    void readFile(HashMap<String, String> hm, HashMap<Integer, String> hmdirec, Signup s, Login l) throws Exception {
        BufferedReader br12 = new BufferedReader(new FileReader("recordDirec.txt"));
        String s12;
        while ((s12 = br12.readLine()) != null) {
            String arr[] = s12.split(" ");
            hmdirec.put(Integer.parseInt(arr[0]), arr[1]);
        }
        Scanner sc = new Scanner(System.in);
        String file;
        String regex = "^.+\\.txt$";
        Pattern pattern = Pattern.compile(regex);
        FileReader fr2 = new FileReader("recordHash.txt");
        BufferedReader br2 = new BufferedReader(fr2);
        String s2;
        while ((s2 = br2.readLine()) != null) {
            String sarray2[] = s2.split(" ");
            hm.put(sarray2[0], sarray2[1]);
        }
        int initialTryFileCheck = 3;
        while (true) {
            if (initialTryFileCheck == 0) {
                throw new IsNotAFile("The entered path/name, file doesn't exist.");
            }
            while (true) {
                System.out.println("ENTER FILE THAT YOU WANT TO READ DATA FROM : ");
                file = sc.nextLine();
                Matcher matcher = pattern.matcher(file);
                if (matcher.matches()) {
                    break;
                } else {
                    System.out.println("Enter a valid file name where it ends with '.txt'!");
                }
            }
            File f;
            if (s == null) {
                f = new File(hmdirec.get(l.userfinal) + "//" + file);
            } else {
                f = new File(hmdirec.get(s.userId) + "//" + file);
            }
            if (f.exists()) {
                break;
            } else {
                System.out.println("Enter valid file that is present in your respective directory!");
                initialTryFileCheck--;
                System.out.println("Attempts left : " + initialTryFileCheck);
            }
        }
        boolean checkFileRead = true;
        int attempsRead = 3;
        while (checkFileRead) {
            if (attempsRead == 0) {
                throw new ReadFileError("--ATTEMPTS OVER--ACCESS DENIED--");
            }
            System.out.println("Enter password of that particular file :  ");
            String passFileCheck = sc.nextLine();
            if (s == null) {
                if (Decrypter.decrypt(hm.get(hmdirec.get(l.userfinal) + "//" + file).toString())
                        .equals(passFileCheck)) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String s1 = br.readLine();
                    String readFileAns = Decrypter.decrypt(s1);
                    System.out.println(readFileAns);
                    checkFileRead = false;
                } else {
                    attempsRead--;
                    System.out.println("--INCORRECT PASSWORD--");
                    System.out.println("Attempts Left : " + attempsRead);
                    System.out.println("--TRY AGAIN--");
                }
            } else {
                if (Decrypter.decrypt(hm.get(hmdirec.get(s.userId) + "//" + file).toString()).equals(passFileCheck)) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String s1 = br.readLine();
                    String readFileAns = Decrypter.decrypt(s1);
                    System.out.println("---CONTENT OF YOUR FILE---");
                    System.out.println(readFileAns);
                    checkFileRead = false;
                } else {
                    attempsRead--;
                    System.out.println("--INCORRECT PASSWORD--");
                    System.out.println("Attempts Left : " + attempsRead);
                    System.out.println("--TRY AGAIN--");
                }
            }
        }
    }
}

public class Project_java {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        HashMap<String, String> hm = new HashMap<>();
        HashMap<Integer, String> hmdirec = new HashMap<>();
        Login il = new Login();
        Signup sup = new Signup();
        Content c = new Content();
        boolean checkCatch = false;
        do {
            while (true) {
                System.out.println("Enter 1 for new Signup : ");
                System.out.println("Enter 2 for Login : ");
                System.out.println("Enter 3 to Exit : ");
                try {
                    if (checkCatch) {
                        sc.nextLine();
                    }
                    choice = sc.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("Enter only integers(1,2,3)!");
                    checkCatch = true;
                }
            }

            switch (choice) {

                case 1:
                    sup.initial_Signup1(hmdirec);
                    int Signupchoice = 0;
                    boolean checkCatchSign = false;
                    do {
                        while (true) {
                            System.out.println("ENTER 1 to add File : ");
                            System.out.println("ENTER 2 to read from a File : ");
                            System.out.println("Enter 3 to Exit : ");
                            try {
                                if (checkCatchSign) {
                                    sc.nextLine();
                                }
                                Signupchoice = sc.nextInt();
                                break;
                            } catch (Exception e) {
                                System.out.println("Enter only integer(1,2,3)!");
                                checkCatchSign = true;
                            }
                        }
                        switch (Signupchoice) {
                            case 1:
                                c.addFile(hm, hmdirec, sup, null);
                                break;

                            case 2:
                                c.readFile(hm, hmdirec, sup, null);
                                break;

                            case 3:
                                break;

                            default:
                                System.out.println("ENTER VALID CHOICE : ");
                                break;
                        }
                    } while (Signupchoice != 3);
                    break;

                case 2:
                    il.initial_Login1();
                    int loginchoice = 0;
                    boolean checkCatchLogin = false;
                    do {
                        while (true) {
                            System.out.println("ENTER 1 to add File : ");
                            System.out.println("ENTER 2 to read from a File : ");
                            System.out.println("Enter 3 to Exit : ");
                            try {
                                if (checkCatchLogin) {
                                    sc.nextLine();
                                }
                                loginchoice = sc.nextInt();
                                break;
                            } catch (Exception e) {
                                System.out.println("Enter only integer(1,2,3)!");
                                checkCatchLogin = true;
                            }
                        }
                        switch (loginchoice) {
                            case 1:
                                c.addFile(hm, hmdirec, null, il);
                                break;

                            case 2:
                                c.readFile(hm, hmdirec, null, il);
                                break;

                            case 3:
                                break;

                            default:
                                System.out.println("ENTER VALID CHOICE : ");
                                break;
                        }
                    } while (loginchoice != 3);
                    break;

                case 3:
                    break;

                default:
                    System.out.println("ENTER VALID CHOICE : ");
                    break;
            }
        } while (choice != 3);
    }
}