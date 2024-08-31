package ApplicationLogic.Demo.exceptions;

public class OutOfRAMException extends RuntimeException{


    public OutOfRAMException()
    {
        super();
    }

    public OutOfRAMException(String s)
    {
    super(s);
    }

}
