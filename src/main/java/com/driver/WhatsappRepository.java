package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class WhatsappRepository {
    HashMap<String,User> UserMap = new HashMap<>();
    HashMap<String, List<User>> GroupMap = new HashMap<>();
    HashMap<Group,List<Message>> GroupMessageMap = new HashMap<>();
    HashMap<Integer,Message> MessageMap = new HashMap<>();

    HashMap<User,List<Message>> UserMessageMap = new HashMap<>();

    public String createUser(String name, String mobile) throws Exception{
        if(UserMap.containsKey(mobile)) throw new Exception("User already exists");
        else{
            User user = new User(name,mobile);
            UserMap.put(mobile,user);
            return "SUCCESS";
        }
    }
    public Group createGroup(List<User> users){
     if(users.size()==2){
         Group group = new Group(users.get(1).getName(), users.size());
         GroupMap.put(group.getName(), users);
         return group;
     }else{
         String name = "Group "+Integer.toString(users.size());
         Group group = new Group(name, users.size());
         GroupMap.put(name,users);
         return group;
     }

    }
    public int createMessage(String content){
        int msgid = MessageMap.size()+1;
        Message message = new Message();
        message.setContent(content);
        Date date = new Date();
        message.setTimestamp(date);
        message.setId(msgid);
        MessageMap.put(msgid,message);
        return msgid;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!GroupMap.containsKey(group.getName())) throw new Exception("Group does not exist");

       List<User> userList = GroupMap.get(group.getName());
       for(User user: userList){
           if(user.getName()!=sender.getName()){
               throw new Exception("You are not allowed to send message");
           }
       }
       if(GroupMessageMap.containsKey(group)){
           List<Message> messageList = GroupMessageMap.get(group);
           messageList.add(message);
           GroupMessageMap.put(group,messageList);
       }else{
       List<Message> messageList = new ArrayList<>();
       messageList.add(message);
       GroupMessageMap.put(group,messageList);
       }
        if(UserMessageMap.containsKey(sender)){
            List<Message> messageList = UserMessageMap.get(sender);
            messageList.add(message);
            UserMessageMap.put(sender,messageList);
        }else{
            List<Message> messageList = new ArrayList<>();
            messageList.add(message);
            UserMessageMap.put(sender,messageList);
        }

       return GroupMessageMap.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!GroupMap.containsKey(group.getName())) throw new Exception("Group does not exist");

        if(approver.getName()!=GroupMap.get(group).get(0).getName()) throw new Exception("Approver does not have rights");

        List<User> userList = GroupMap.get(group.getName());
        for(User thisuser: userList){
            if(thisuser.getName()!=user.getName()){
                throw new Exception("User is not a participant");
            }
        }
        userList.add(0,user);
        GroupMap.put(group.getName(),userList);
        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception{
        boolean t = false;
        int sum = 0;
        for(List<User> userList: GroupMap.values()){
            for(int i=0; i<userList.size(); i++){
                if(userList.get(i).getName()==user.getName() && i!=0)
                {   sum += UserMessageMap.get(user).size();
                    userList.remove(i);
                    t = true;
                    sum+=userList.size();
                }
                if(userList.get(i).getName()==user.getName() && i==0)
                { throw new Exception("Cannot remove admin");
                }
            }
        }
        sum+= MessageMap.size();
        if(t){
            return sum;
        }else throw new Exception("User not found");

    }
}
