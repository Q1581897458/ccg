package com.mr.listener;

import com.mr.common.routintket.MqMessageConstant;
import com.mr.service.FileStaticService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodListener {

    @Autowired
    private FileStaticService fileStaticService;

    public GoodListener(FileStaticService fileStaticService) {
        this.fileStaticService = fileStaticService;
    }

    /**
     *创建 rabbit监听，创建队列，绑定到交换机，
     * 设置持久化，以及，交换机类型等
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqMessageConstant.SPU_QUEUE_PAGE_SAVE, durable = "true"),
            exchange = @Exchange(
                    value = MqMessageConstant.SPU_EXCHANGE_NAME,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {MqMessageConstant.SPU_ROUT_KEY_SAVE,MqMessageConstant.SPU_ROUT_KEY_UPDATE}))
    public void createHtml(String id){
        System.out.println("page执行新增覆盖：" + id);
        try{
            fileStaticService.createStaticHtml(Long.valueOf(id));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqMessageConstant.SPU_QUEUE_PAGE_DELETE, durable = "true"),
            exchange = @Exchange(
                    value = MqMessageConstant.SPU_EXCHANGE_NAME,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {MqMessageConstant.SPU_ROUT_KEY_DELETE}))
    public void delPage(String id){
        System.out.println("page执行删除：" + id);
        try{
            fileStaticService.deleteStaticHtml(Long.valueOf(id));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
