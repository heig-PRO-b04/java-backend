package ch.heigvd.pro.b04.utils;

import java.util.ArrayList;
import java.util.Random;

public class IdGenerator {
  private static ArrayList<Boolean[]> factors=new ArrayList<>();

  private IdGenerator()
  {
    for(short x=0;x<Constants.IDGEN_NB_ARRAY;++x)
    {
      factors.add(new Boolean[Constants.IDGEN_SIZE_ARRAY]);
      for(short y=0;y<Constants.IDGEN_SIZE_ARRAY;++y)
      {
        factors.get(x)[y]=false;
      }
    }
  }

  public static long newId()
  {
    short nbTrues=0;
    Random rand=new Random();
    long id=1;

    for(short v=0;v<Constants.IDGEN_NB_ARRAY;++v)
    {
      int index=rand.nextInt(Constants.IDGEN_SIZE_ARRAY);
      if (factors.get(v)[index])
      {
        ++nbTrues;
      }
      factors.get(v)[index]=true;
      id*=(index+1+(v*Constants.IDGEN_SIZE_ARRAY));
    }

    //if and only if nbTrues==4, this id has already been generated and
    //it needs to be recalculated
    if(nbTrues==Constants.IDGEN_NB_ARRAY)
    {
      return newId();
    }

    return id;
  }
}
