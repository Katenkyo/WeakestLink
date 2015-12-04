package WeakestLink.Players;
import WeakestLink.Game.Vote;

import java.util.*;
import java.awt.Point;

public class ApproximatePosition extends Player
{

    @Override
    public int vote(Set<Integer> currentOpponent)
    {
        List<Integer> present = new ArrayList<>(currentOpponent);
        List<Integer> emptyPosition = new ArrayList<Integer>();
        Collections.sort(present);
        
        //If it is the first round, vote for the smartest buddy
        if(present.size()==8)
            return present.get(present.size()-1);


        int lastCheck=present.get(0);
        if(lastCheck>0)
        	for(int i=0;i<lastCheck;i++)
        		if(i!=getSmartness()&&!emptyPosition.contains(i))
        			emptyPosition.add(i);
        for(int i=1;i<present.size();i++)
        {
            if(present.get(i)-lastCheck>1)
                for (int j=lastCheck+1;j<present.get(i);j++)
                    if(j!=getSmartness()&&!emptyPosition.contains(j))
                        emptyPosition.add(j);
            lastCheck=present.get(i);
        }
        //untill there's at least 3 excluded members, we continue with this behaviour
        if(emptyPosition.size()<=2)
        {
        	if(emptyPosition.isEmpty()) return present.get(present.size()-1);
            return decide(emptyPosition.get(0),present.get(present.size()-1),present.get(0),present);
        }
      
        Point maxRangeOfBlank=new Point(present.get(present.size()-1),present.get(present.size()-1));
        for (int i=0;i<emptyPosition.size()-1;i++)
            if(emptyPosition.get(i+1)-emptyPosition.get(i)==1)
            {
                int size=0;
                while(i+size+1<emptyPosition.size() && emptyPosition.get(i+size+1)-emptyPosition.get(i+size)==1)
                    size++;
                if(size>=sizeOfRange(maxRangeOfBlank))
                    maxRangeOfBlank=new Point(emptyPosition.get(i),emptyPosition.get(size));
                i+=size;
            }

        return decide(maxRangeOfBlank,present.get(present.size()-1),present.get(0),present);
    }

    private int decide(int blankSeat, int smartest,int dumbest,List<Integer> present)
    {
        return decide(new Point(blankSeat,blankSeat),smartest,dumbest,present);
    }

    private int decide(Point rangeBlankSeat, int smartest,int dumbest,List<Integer> present)
    {
    	int target= smartest;
        if (rangeBlankSeat.getY()==smartest||((int)rangeBlankSeat.getY()+1)==getSmartness()){
            if ((rangeBlankSeat.getX()==dumbest||(int)rangeBlankSeat.getX()-1==getSmartness())){
                target= smartest; //should not happen
            } else {
                target= (int) rangeBlankSeat.getX()-1; //Vote for dumber than the missing
            }
        } else {
            target= (int) rangeBlankSeat.getY() +1; //Vote for smarter than the missing, default comportment
        }
        if(present.contains(target))
        	return target;
        return smartest;
    }
    //Return the number of consecutive values between X and Y (included)
    private int sizeOfRange(Point range)
    {
        return (int)(range.getY()-range.getX())+1;
    }
 
}
