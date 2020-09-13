package com.milchstrabe.rainbow.biz.repository;

import com.milchstrabe.rainbow.server.domain.po.AddContactMessage;
import com.milchstrabe.rainbow.server.domain.po.Message;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepository {


    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *    private String id;
     *     private Integer msgType;
     *     private T content;
     *     private String sender;
     *     private String receiver;
     *     private Short status;
     *     private Long date;
     * @return
     */
    public List<Message> getAddContactMessage(String userId){
        Query query = new Query();
        query.addCriteria(Criteria.where("receiver").is(userId));
        List<Message> messages = mongoTemplate.find(query, Message.class);
        return messages;
    }

    public Message getAddContactMessage(String sender,String receiver){
        Query query = new Query();
        query.addCriteria(Criteria.where("receiver").is(receiver).and("sender").is(sender).and("content.status").is(0));
        Message messages = mongoTemplate.findOne(query, Message.class);
        return messages;
    }

    public boolean addContactMessage(Message<AddContactMessage> message){
        mongoTemplate.save(message);
        return true;
    }

    public boolean handleAddContact(String userId,String sender,Short handle){
        Query query = new Query();
        query.addCriteria(Criteria.where("receiver").is(userId).and("sender").is(sender).and("content.status").is(0));
        Update update = new Update();
        update.set("content.status",handle);
        update.set("status",2);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Message.class);
        if(updateResult.getModifiedCount() > 0){
            return true;
        }else{
            return false;
        }
    }
}
