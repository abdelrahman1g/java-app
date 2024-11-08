import java.util.LinkedList;
import java.util.Queue;

class Message {
    private String content;

    public Message(String content) {
        this.content = content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

class MessageQueue {
    private Queue<Message> queue = new LinkedList<>();

    public synchronized void sendMessage(Message message) {
        queue.add(message);
        notify();
    }

    public synchronized Message receiveMessage() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.poll();
    }
}

class Sender extends Thread {
    private MessageQueue queue;

    public Sender(MessageQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Message message = new Message("Ana Abdelrahman");
            queue.sendMessage(message);
            System.out.println("El Name");
            Thread.sleep(1000);
            message.setContent("Da task Dr Ashraf");
            queue.sendMessage(message);
            System.out.println("El task");
            Thread.sleep(1000);

            System.out.println("W da synchronized test");
            for (int i = 0; i < 5; i++) {
                message.setContent("Message " + i);
                queue.sendMessage(message);
                System.out.println("Sent: " + message.getContent());
                Thread.sleep(1000);
            }
            queue.sendMessage(new Message("END"));
        }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
        }
    }
}

class Receiver extends Thread {
    private MessageQueue queue;

    public Receiver(MessageQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (queue != null) {
            try {
                Message message = queue.receiveMessage();
                if ("END".equals(message.getContent())) {
                    System.out.println("Receiver received termination signal. Exiting.");
                    break;
                }
                System.out.println("Received: " + message.getContent());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class MessagePassing {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue();
        Sender sender = new Sender(queue);
        Receiver receiver = new Receiver(queue);

        sender.start();
        receiver.start();
    }


}
