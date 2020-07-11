package mqdemo.rpcdemo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import mqdemo.RabbitMqUtils;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * @author 小白i
 * @date 2020/7/8
 */
public class RpcClient {

    private String replyQueueName;

    private QueueingConsumer consumer;

    private Channel channel;

    public RpcClient() throws Exception {
        channel = RabbitMqUtils.getChannel();
        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public String call(String message) throws Exception {
        String response;
        String uuid = UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties().builder()
                .correlationId(uuid)
                .replyTo(replyQueueName)
                .build();
        channel.basicPublish("", replyQueueName, props, message.getBytes());

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (StringUtils.equals(delivery.getProperties().getCorrelationId(), uuid)) {
                response = new String(delivery.getBody());
                break;
            }
        }
        return response;
    }

    public static void main(String[] args) throws Exception {
        RpcClient fibRpc = new RpcClient();
        System.out.println(" [x) Requesting fib(30)");
        String response = fibRpc.call("30");
        System.out.println(" [.) Got "+response);
        //fibRpc.close();
    }

}
