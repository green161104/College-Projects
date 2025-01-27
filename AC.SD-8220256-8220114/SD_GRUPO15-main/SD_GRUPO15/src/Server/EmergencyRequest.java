package Server;

import Authentication.User;
import Enums.UserProfiles;

public class EmergencyRequest{

    public static int emergencyRequestID = 1;
    public EmergencyMessage emergencyRequest;
    public long timestamp;
    public boolean response;

    public EmergencyRequest(UserProfiles requesterAuthLvl, String message){
        emergencyRequest = new EmergencyMessage(message, requesterAuthLvl);
        timestamp = System.currentTimeMillis();
        response = false;
        emergencyRequestID++;
        System.out.println("this is the request at emergencyRequest class time of creation " + this.emergencyRequest);
    }

    @Override
    public String toString(){
        return ( "created by " + this.emergencyRequestID +  "with emergency message " + emergencyRequest + "at" + this.timestamp);
    }



}
