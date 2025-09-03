package gm.PaynTime.utilities;

import java.util.ArrayList;

import javafx.scene.control.Alert;

public class ControllerGeneralModel {
	
	
	public static int enumSizeExcepcion(ArrayList<String>list){
        return list.size()+1;
    }
    
//    public static String toString(ArrayList<String> datos){
//        String text="";
//        for (int i = 0; i < datos.size(); i++) {
//            text=text+datos.get(i)+"\n";
//        }
//        return text;
//    }    

    public static boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);                
    }
    
    
	public static void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
	
	public static String trim(String text){
		return text == null ? "" : text.trim();
    	
    }
}
