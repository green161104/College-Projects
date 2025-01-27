package Server;

import Authentication.User;
import Enums.UserProfiles;

public class EmergencyMessage {

    public static int id = 1;
    String message;
    UserProfiles requiredAuthLevel;
    long timestamp;

    public EmergencyMessage(String message, UserProfiles requiredAuthLevel) {
        this.id = id++; // começa a 1 e segue em frente
        this.message = message; // alert message
        this.requiredAuthLevel = requiredAuthLevel; // auth lvl do user q faz o pedido
    }

    @Override
    public String toString(){
       return ( "created by " + this.id + "with the message : " + this.message + this.requiredAuthLevel + "at" + this.timestamp);
    }
}
