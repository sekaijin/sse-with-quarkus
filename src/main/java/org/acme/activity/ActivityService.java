package org.acme.activity;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ActivityService
{

   static long lastId;

   public Activity next(){
      return new Activity(lastId++);
   }

}
