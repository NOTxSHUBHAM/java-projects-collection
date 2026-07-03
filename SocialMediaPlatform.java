import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

// ─── Post ───
class Post {
    static AtomicInteger counter = new AtomicInteger(1);
    int id;
    String author, content, timestamp;
    AtomicInteger likes = new AtomicInteger(0);

    public Post(String author, String content) {
        this.id        = counter.getAndIncrement();
        this.author    = author;
        this.content   = content;
        this.timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public void like()   { likes.incrementAndGet(); }

    @Override
    public String toString() {
        return String.format("[Post #%d] @%-12s | %s | Likes: %d | '%s'",
                id, author, timestamp, likes.get(), content);
    }
}

// ─── User Profile ───
class UserProfile {
    String username;
    List<String>  friends  = new CopyOnWriteArrayList<>();
    List<Post>    posts    = new CopyOnWriteArrayList<>();
    BlockingQueue<String> notifications = new LinkedBlockingQueue<>();
    boolean isPrivate;

    public UserProfile(String username, boolean isPrivate) {
        this.username  = username;
        this.isPrivate = isPrivate;
    }

    public void addFriend(String friend) {
        if (!friends.contains(friend)) {
            friends.add(friend);
            notifications.add("You are now friends with @" + friend);
        }
    }

    public Post createPost(String content) {
        Post p = new Post(username, content);
        posts.add(p);
        return p;
    }

    public void notify(String msg) { notifications.add(msg); }

    public void showProfile() {
        System.out.println("\n===== Profile: @" + username + " (" + (isPrivate ? "Private" : "Public") + ") =====");
        System.out.println("Friends: " + friends);
        System.out.println("Posts  : " + posts.size());
        posts.forEach(p -> System.out.println("  " + p));
    }
}

// ─── Social Media Platform ───
class SocialPlatform {
    Map<String, UserProfile> users = new ConcurrentHashMap<>();
    List<Post> newsFeed = new CopyOnWriteArrayList<>();
    ExecutorService pool = Executors.newFixedThreadPool(5);

    public void registerUser(String username, boolean isPrivate) {
        users.put(username, new UserProfile(username, isPrivate));
        System.out.println("User @" + username + " registered.");
    }

    public void createPost(String username, String content) {
        UserProfile user = users.get(username);
        if (user == null) { System.out.println("User not found."); return; }
        Post post = user.createPost(content);
        newsFeed.add(post);

        // Notify friends asynchronously
        pool.submit(() -> {
            for (String friend : user.friends) {
                UserProfile f = users.get(friend);
                if (f != null) f.notify("@" + username + " posted: " + content);
            }
        });

        System.out.println("Posted: " + post);
    }

    public void addFriend(String user1, String user2) {
        UserProfile u1 = users.get(user1);
        UserProfile u2 = users.get(user2);
        if (u1 == null || u2 == null) { System.out.println("User not found."); return; }
        if (u2.isPrivate) { System.out.println("@" + user2 + "'s profile is private. Send request instead."); return; }
        u1.addFriend(user2);
        u2.addFriend(user1);
        System.out.println("@" + user1 + " and @" + user2 + " are now friends!");
    }

    public void likePost(String username, int postId) {
        pool.submit(() -> {
            for (Post p : newsFeed) {
                if (p.id == postId) {
                    p.like();
                    UserProfile author = users.get(p.author);
                    if (author != null) author.notify("@" + username + " liked your post #" + postId);
                    System.out.println("@" + username + " liked Post #" + postId);
                    return;
                }
            }
            System.out.println("Post not found.");
        });
    }

    public void showNewsFeed() {
        System.out.println("\n===== News Feed =====");
        if (newsFeed.isEmpty()) System.out.println("Nothing here yet.");
        else newsFeed.stream().sorted(Comparator.comparingInt((Post p) -> p.id).reversed())
                     .forEach(System.out::println);
    }

    public void showNotifications(String username) {
        UserProfile user = users.get(username);
        if (user == null) { System.out.println("User not found."); return; }
        System.out.println("\n===== Notifications for @" + username + " =====");
        List<String> notifs = new ArrayList<>(user.notifications);
        if (notifs.isEmpty()) System.out.println("No notifications.");
        else notifs.forEach(n -> System.out.println("  • " + n));
    }

    public void shutdown() { pool.shutdown(); }
}

// ─── Main ───
public class SocialMediaPlatform {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("===== Social Media Platform (Multithreading Demo) =====\n");

        SocialPlatform platform = new SocialPlatform();

        // Register users
        platform.registerUser("alice",   false);
        platform.registerUser("bob",     false);
        platform.registerUser("charlie", true); // private
        platform.registerUser("dave",    false);

        // Add friends
        System.out.println("\n--- Adding Friends ---");
        platform.addFriend("alice", "bob");
        platform.addFriend("alice", "dave");
        platform.addFriend("bob",   "dave");
        platform.addFriend("alice", "charlie"); // should block (private)

        // Create posts via multiple threads
        System.out.println("\n--- Creating Posts ---");
        ExecutorService postPool = Executors.newFixedThreadPool(4);
        postPool.submit(() -> platform.createPost("alice",   "Hello world! First post!"));
        postPool.submit(() -> platform.createPost("bob",     "Java multithreading is awesome!"));
        postPool.submit(() -> platform.createPost("dave",    "Good morning everyone!"));
        postPool.submit(() -> platform.createPost("charlie", "Private thoughts..."));
        postPool.shutdown();
        postPool.awaitTermination(3, TimeUnit.SECONDS);

        // Like posts
        System.out.println("\n--- Liking Posts ---");
        platform.likePost("bob",   1);
        platform.likePost("alice", 2);
        platform.likePost("dave",  1);
        Thread.sleep(300);

        // Show news feed
        platform.showNewsFeed();

        // Show profiles
        platform.users.get("alice").showProfile();
        platform.users.get("bob").showProfile();

        // Show notifications
        platform.showNotifications("alice");
        platform.showNotifications("bob");

        platform.shutdown();
        System.out.println("\nPlatform session ended.");
    }
}
