package p1;

public class SportEvent {
    private int eventId;
    private String name;
    private String type;

    public SportEvent(int eventId,String name,String type){
        this.eventId = eventId;
        this.name    = name;
        this.type    = type;
    }
    public int getEventId(){return eventId;}
    public String getName(){return name;}
    public String geyType(){return type;}

    public void setEventId(int eventId){this.eventId = eventId;}

    public void setName(String name){this.name = name;}
    public void setType(String type){this.type = type;}


    
}
