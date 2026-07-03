import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

// ─── Message class ───
class Message {
    String sender, text, timestamp;

    public Message(String sender, String text) {
        this.sender    = sender;
        this.text      = text;
        this.timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp, sender, text);
    }
}

// ─── Chat Room (shared resource) ───
class ChatRoom {
    private final List<Message> messageHistory = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final String roomName;

    public ChatRoom(String name) { this.roomName = name; }

    public void sendMessage(Message msg) {
        lock.lock();
        try {
            messageHistory.add(msg);
            System.out.println("  >> " + msg);
        } finally { lock.unlock(); }
    }

    public void showHistory() {
        lock.lock();
        try {
            System.out.println("\n===== Chat History: " + roomName + " =====");
            if (messageHistory.isEmpty()) System.out.println("No messages yet.");
            else messageHistory.forEach(System.out::println);
        } finally { lock.unlock(); }
    }

    public int getMessageCount() { return messageHistory.size(); }
}

// ─── User (each user runs on its own thread) ───
class ChatUser implements Runnable {
    private String username;
    private ChatRoom room;
    private List<String> messagesToSend;
    private int delayMs;

    public ChatUser(String username, ChatRoom room, List<String> messages, int delayMs) {
        this.username       = username;
        this.room           = room;
        this.messagesToSend = messages;
        this.delayMs        = delayMs;
    }

    @Override
    public void run() {
        System.out.println("[" + username + "] connected to chat.");
        for (String text : messagesToSend) {
            try {
                Thread.sleep(delayMs);
                room.sendMessage(new Message(username, text));
            } catch (InterruptedException e) {
                System.out.println("[" + username + "] was interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + username + "] left the chat.");
    }
}

// ─── Notification Service (daemon thread) ───
class NotificationService implements Runnable {
    private ChatRoom room;
    private volatile boolean running = true;
    private int lastCount = 0;

    public NotificationService(ChatRoom room) { this.room = room; }

    public void stop() { running = false; }

    @Override
    public void run() {
        System.out.println("[Notification Service] Started.");
        while (running) {
            try {
                Thread.sleep(500);
                int current = room.getMessageCount();
                if (current > lastCount) {
                    System.out.println("  [NOTIFY] " + (current - lastCount) + " new message(s) received.");
                    lastCount = current;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); break;
            }
        }
        System.out.println("[Notification Service] Stopped.");
    }
}

// ─── Main ───
public class ChatApplication {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("===== Real-Time Chat Application (Multithreading Demo) =====");

        ChatRoom room = new ChatRoom("General");

        // Notification service runs in background
        NotificationService notifier = new NotificationService(room);
        Thread notifyThread = new Thread(notifier);
        notifyThread.setDaemon(true);
        notifyThread.start();

        // Create users with pre-set messages (simulating real chat)
        ChatUser alice = new ChatUser("Alice", room,
                Arrays.asList("Hey everyone!", "How's it going?", "Anyone want to work on the project?"), 300);
        ChatUser bob   = new ChatUser("Bob", room,
                Arrays.asList("Hi Alice!", "I'm good, thanks.", "Sure, let's do it!"), 400);
        ChatUser charlie = new ChatUser("Charlie", room,
                Arrays.asList("Hello all!", "What project?", "Count me in!"), 350);

        // Use Thread Pool
        ExecutorService pool = Executors.newFixedThreadPool(3);
        System.out.println("\n--- Users joining chat ---");
        pool.submit(alice);
        pool.submit(bob);
        pool.submit(charlie);

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        Thread.sleep(600); // Let notifier catch up

        notifier.stop();
        room.showHistory();

        System.out.println("\nTotal messages sent: " + room.getMessageCount());
        System.out.println("Chat session ended.");
    }
}
