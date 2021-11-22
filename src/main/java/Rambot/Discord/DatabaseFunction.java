package Rambot.Discord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class DatabaseFunction {
    public static void register(String idServ,String idUser,String userName) throws SQLException {
        Statement statement = Rambot.connection.createStatement();
        try {
            statement.executeUpdate("INSERT INTO `user`(`idUser`, `idServeur`,`nameUser`, `bourse`, `affection`) VALUES"+
                    "(\""+idUser+"\",\""+idServ+"\",\""+userName+"\",0,0);");
        } catch (Exception e){ e.printStackTrace(); }
        return;
    }
    public static void ServRegister(String idServ,String servName) throws SQLException {
        Statement statement = Rambot.connection.createStatement();
        try {
            statement.executeUpdate("INSERT INTO `server`(`id`,`name`) VALUES  (\""+idServ+"\",\""+servName+"\")");
        } catch (Exception e){ }
        return;
    }
    public static boolean checkUser(String idUser,String idServ) throws SQLException {
        boolean check = true;
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM user where idUser=\""+idUser+"\" AND idServeur=\""+idServ+"\"");
        if (rs.next()==true){
            check = false;
        }
        return check;
    }

    public static boolean setLang(String guildId,int langId) throws SQLException {
        boolean res = false;
        Statement statement = Rambot.connection.createStatement();
        try {
            statement.executeUpdate("UPDATE `server` SET `idLangue` ="+langId+" WHERE `id` ="+guildId);
            res = true;
        } catch (Exception e) {
            return res;
        }
        return res;
    }

    public static String getIdBot(String userId,String guildId) throws SQLException {
        String res="";
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT idBot FROM user WHERE idUser=\""+userId+"\" AND idServeur=\""+guildId+"\"");
        while (rs.next()) {
            res = rs.getString("idBot");
        }
        return res;
    }

    public static String getProfile(String idBot) throws SQLException {
        String res="";
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from user where idBot="+idBot);
        while (rs.next()) {
            res = rs.getString("idBot")+"-"+rs.getString("idUser")+"-"+rs.getString("affection")+"-"+rs.getString("bourse")+rs.getString("teamComp");
        }
        return res;
    }

    public static String[][] shop () throws  SQLException {
        String [][] shop;
        int i, j, n;
        int length = 1;
        String res ="";
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM `produits`");
        while (rs.next()) {
            res += rs.getString("id")+"-"+rs.getString("nom")+"-"+rs.getString("nomFr")+"-"+rs.getString("description")+
                    "-"+rs.getString("prix")+"-"+rs.getString("icone")+"-";
            length++;
        }
        System.out.println(res);
        String[] infoRes = res.split("-");
        shop = new String[length][6];
        shop[0][0] ="id" ;
        shop[0][1] ="nom" ;
        shop[0][2] ="nomFr" ;
        shop[0][3] ="description" ;
        shop[0][4] ="prix" ;
        shop[0][5] ="icone" ;
        for (n = 0;n< infoRes.length;) {
            for (i = 1; i < length; i++) {
                for (j = 0; j < shop[i].length; j++) {
                    shop[i][j] = infoRes[n];
                    n++;
                }
            }
        }
        return shop;
    }
    public static String[] buy (String idBot,int bourse,String item,int lot) throws  SQLException {
        String res ="";
        Statement statement = Rambot.connection.createStatement();
        int prix;
        ResultSet rs = statement.executeQuery("SELECT * FROM `produits` WHERE nom = \""+item+"\"or nomFr=\""+item+"\"");
        while (rs.next()) {
            res += rs.getString("id")+"-"+rs.getString("nom")+"-"+rs.getString("nomFr")+
                    rs.getString("description")+"-"+rs.getString("prix")+"-"+rs.getString("icone");
        }
        String[] infoRes = res.split("-");
        if (infoRes.length==1){
            return infoRes;
        }
        prix = parseInt(infoRes[3])*lot;
        String[]trueInfoRes = Arrays.copyOf(infoRes,infoRes.length+1);
        String transaction= (infoRes[0]+"-").repeat(lot);
        if (bourse>=prix) {
            statement.executeUpdate("UPDATE `user` SET `bourse`=bourse-" + prix + " WHERE idBot = " + idBot);
            statement.executeUpdate("UPDATE `inventaire` set `Objets`=CONCAT(Objets,\"" + transaction + "\") where id=" + idBot);
            trueInfoRes[5] = "yes";
        }else{
            trueInfoRes[5] = "no";
        }
        return trueInfoRes;

    }
    public static int getSomme(String idBot) throws SQLException {
        int res = 0;
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT `bourse` FROM `user` WHERE idBot="+idBot);
        while (rs.next()) {
            res = parseInt(rs.getString("bourse"));
        }
        return res;
    }
    public static String returnStringIdObjets(String idBot,String nature) throws SQLException {
        String res="";
        String ArmeeBDD="";
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("Select * from inventaire where id ="+idBot);
        while (rs.next()){
            ArmeeBDD = rs.getString(nature);
        }
        String[] Armee = ArmeeBDD.split("-");
        for (int i = 0;i<Armee.length;i++){
            res += "id ="+Armee[i];
            if (i<Armee.length-1){
                res += " or ";
            }
        }

        return res;
    }
    public static String[][] returnIdIconeObjets (String inventaire) throws SQLException {
        int length=1;
        String icone = "";
        String id = "";
        String nom = "";
        String nomFr = "";
        Statement statement = Rambot.connection.createStatement();
        if (inventaire.equals("")){
            return null;
        }else {
            ResultSet rs = statement.executeQuery("SELECT id,icone,nom,nomFr FROM `produits` WHERE " + inventaire+" ORDER BY `id`");
            while (rs.next()) {
                icone += rs.getString("icone")+"-";
                id += rs.getString("id")+"-";
                nom += rs.getString("nom")+"-";
                nomFr += rs.getString("nomFr")+"-";
                length++;
            }
        }
        int z = 0;
        String[] infoRes = inventaire.replace("id =","").replace(" ","").split("or");
        String[][] arrayObjets = new String[length][5];
        for (int row = 0; row < arrayObjets.length; row++)
        {
            for (int col = 0; col < arrayObjets[row].length; col++)
            {
                arrayObjets[row][col] = "0"; //Whatever value you want to set them to
            }
        }

        String[] arrayIcone = icone.split("-");
        String[] arrayId = id.split("-");
        String[] arrayNom = nom.split("-");
        String[] arrayNomFr = nomFr.split("-");
        arrayObjets[0][0] ="id";
        arrayObjets[0][1] ="qtt";
        arrayObjets[0][2] ="nom";
        arrayObjets[0][3] ="nomFr";
        arrayObjets[0][4] ="icone";
        for (int i = 1; i <= arrayId.length; i++) {
            arrayObjets[i][0] = arrayId[i-1];
            for (int j = 0 ; j < infoRes.length; j++) {
                if (infoRes[j].equals(arrayObjets[i][0]))
                    arrayObjets[i][1] = String.valueOf(parseInt((arrayObjets[i][1]))+1);
            }
            for (int k = 0 ; k < infoRes.length; k++) {
                if (infoRes[k].equals(arrayObjets[i][0]))
                    arrayObjets[i][2] = arrayNom[i-1];
            }
            for (int l = 0 ; l < infoRes.length; l++) {
                if (infoRes[l].equals(arrayObjets[i][0]))
                    arrayObjets[i][3] = arrayNomFr[i-1];
            }
            for (int m = 0 ; m < infoRes.length; m++) {
                if (infoRes[m].equals(arrayObjets[i][0]))
                    arrayObjets[i][4] = arrayIcone[i-1];
            }
        }
        /*for (int i = 0; i < arrayObjets.length; i++)
            System.out.println(Arrays.toString(arrayObjets[i]));*/
        return arrayObjets;
    }
    public static String[] roll(String idBot,int n) throws SQLException {
        boolean triggerCompensation = false;
        String res = "pouet-pouet";
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM personnage WHERE StarRate = " + n + " ORDER BY RAND() LIMIT 1");
        while (rs.next()) {
            res = rs.getString("Nom") + "-" + rs.getString("img") + "-" + rs.getString("imgIcone") + "-" + rs.getString("id");
        }
        String[] infoRes = res.split("-");
        String[] idArmee = returnStringIdArmy(idBot);
        for (int i = 0; i < idArmee.length; i++) {
            if (idArmee[i].equals(infoRes[3])) {
                triggerCompensation = true;
            }
        }
        String[]trueInfoRes = Arrays.copyOf(infoRes,infoRes.length+1);
        if (triggerCompensation == false) {
            statement.executeUpdate("UPDATE `inventaire` set `army`=CONCAT(army,\"" + infoRes[3] + "-\") where id=" + idBot);
            trueInfoRes[4] = "no";
        }
        else {
            loot(idBot,500);
            trueInfoRes[4] = "yes";
        }
        return trueInfoRes;
    }
    public static String[] returnStringIdArmy(String idBot) throws SQLException {
        String res="";
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT `Army` FROM `inventaire` WHERE id="+idBot);
        while (rs.next()) {
            res += rs.getString("Army");
        }
        String[] idArmee = res.split("-");
        return idArmee;
    }
    public static void loot(String idBot,int n) throws SQLException {
        Statement statement = Rambot.connection.createStatement();
        statement.executeUpdate("UPDATE `user` SET `bourse`=bourse+"+n+" WHERE idBot = "+idBot);
    }
    public static boolean spent(String idBot,String idItem) throws SQLException {
        String tardigrade = "";
        boolean check = false;
        String res ="";
        int newqtt=-1;
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT Objets FROM `inventaire` WHERE `id` ="+idBot);
        while (rs.next()) {
            res = rs.getString("Objets");
        }
        System.out.println(res);
        String[] infoRes = res.split("-");
        String[] itemCheck=new String[infoRes.length];
        Arrays.fill(itemCheck,"x");
        String[] itemElse=new String[infoRes.length];
        Arrays.fill(itemElse,"x");
        for (int n=0;n<infoRes.length;n++){
            if(infoRes[n].equals(idItem)){
                itemCheck[n] = infoRes[n];
            }else{
                itemElse[n] = infoRes[n];
            }
        }
        for (int a = 0;a<itemCheck.length;a++){
            if(itemCheck[a].equals(idItem)){
                check=true;
                newqtt++;
            }
        }
        String test="";
        String test2="";
        for (int a = 0;a<itemCheck.length;a++)
            test+=itemCheck[a];
        for (int f = 0;f<itemElse.length;f++)
            test2+=itemElse[f];
        System.out.println(test);
        System.out.println(test2);
        if (check==true) {
            int count = 0;
            System.out.println(itemCheck.length);
            for (int r = 0; r < itemCheck.length; r++) {
                if (count == newqtt) {
                    break;
                }
                if (itemCheck[r].equals(idItem)) {
                    tardigrade += idItem + "-";
                    count++;
                }
            }
            for (int r = 0; r < itemElse.length; r++) {
                if (!itemElse[r].equals("x")) {
                    tardigrade += itemElse[r] + "-";
                }
            }
            System.out.println(itemCheck.length);
            System.out.println(itemElse.length);
            System.out.println(tardigrade);
            statement.executeUpdate("UPDATE `inventaire` SET `Objets`=\""+tardigrade+"\" WHERE id="+idBot);
        }
        return check;
    }
    public static String returnArmee (String armee) throws SQLException {
        String res = "";
        Statement statement = Rambot.connection.createStatement();
        if (armee.equals("")) {
        } else {
            ResultSet rs = statement.executeQuery("SELECT imgIcone FROM `iersonnage` WHERE " + armee);
            while (rs.next()) {
                res += rs.getString("imgIcone");
            }
        }
        return res;

    }
    public static Boolean shifumi(int emote,int emoteBot){
        Boolean res;
        //0)ROCK:1)PAPER:2)SCISSORS
        if (emote==emoteBot){
            res = null;
        }
        else if (emote==0 && emoteBot==2||emote==2 && emoteBot==1||emote==1 && emoteBot==0){
            res = true;
        }else{
            res = false;
        }
        return res;

    }
}
