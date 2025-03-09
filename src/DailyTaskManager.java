import java.util.Scanner;
import java.util.Stack;

// Daftar task yang akan dilakukan oleh user, dimana mereka dapat mengeditnya
class TaskArray {
    private String[] tasks = {"Eat", "Workout", "Study", "Read a book", "Cooking"};
    private Stack<TaskUpdate> undoStack = new Stack<>();

    // Menampilkan daftar task tersebut 
    public void displayTasks() {
        for (int i = 0; i < tasks.length; i++)
            System.out.println((i + 1) + ". " + tasks[i]);
    }

    // Mengupdate task tertentu yang diinput user dan menambahkannya ke stack agar bisa di undo kembali 
    public void updateTask(int index, String newTask) {
        if (index >= 0 && index < tasks.length) {
            undoStack.push(new TaskUpdate(index, tasks[index]));
            tasks[index] = newTask;
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Invalid index.");
        }
    }
     // Menundo task kembali seperti semula sebelum perubahan yang terakhir dilakukan
    public void undoUpdate() {
        if (!undoStack.isEmpty()) {
            TaskUpdate lastUpdate = undoStack.pop();
            tasks[lastUpdate.index] = lastUpdate.oldTask;
            System.out.println("Undo successful: Task reverted.");
        } else {
            System.out.println("No updates to undo.");
        }
    }
}

// Digunakan untuk menyimpan task task yang diubah/diupdate oleh user
class TaskUpdate {
    int index;
    String oldTask;
    public TaskUpdate(int index, String oldTask) {
        this.index = index;
        this.oldTask = oldTask;
    }
}

// Digunakan untuk menyimpan task yang telah selesai dilakukan 
class TaskStack {
    private Stack<String> stack = new Stack<>();
    // Menambahkan task yang telah selesai dilakukan
    public void push(String task) {
        stack.push(task);
        System.out.println("Task added to undo stack.");
    }
    // Menghapus task yang terakhir diselesaikan
    public String pop() {
        return stack.isEmpty() ? "Undo stack is empty." : stack.pop();
    }
    // Memeriksa task terakhir yang dilakukan tanpa menghapusnya
    public String peek() {
        return stack.isEmpty() ? "Undo stack is empty." : stack.peek();
    }
}
// Digunakan untuk mengelola task task dalam linked list
class TaskLinkedList {
    private Node head;
    private Stack<RemovedTask> removedTasks = new Stack<>();
// Menambahkan task ke dalam linked list
    public void addTask(String task) {
        Node newNode = new Node(task);
        if (head == null) head = newNode;
        else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
        System.out.println("Task added to the list.");
    }
// Menghapus task dari linked list tersebut dan menyimpannya di stack agar bisa di undo nanti
    public void removeTask(String task) {
        if (head == null) {
            System.out.println("Task list is empty.");
            return;
        }
        if (head.task.equals(task)) {
            removedTasks.push(new RemovedTask(head, null)); 
            head = head.next;
            System.out.println("Task removed successfully.");
            return;
        }
        Node prev = head;
        Node temp = head.next;
        while (temp != null && !temp.task.equals(task)) {
            prev = temp;
            temp = temp.next;
        }
        if (temp != null) {
            removedTasks.push(new RemovedTask(temp, prev)); // Disimpan disini task yang sebelumnya dihapus agar bisa di undo kembali
            prev.next = temp.next;
            System.out.println("Task removed successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    // Menampilkan semua task yang ada di dalam linked list
    public void displayTasks() {
        for (Node temp = head; temp != null; temp = temp.next)
            System.out.println("- " + temp.task);
    }
    // Mengembalikan kembali task yang telah dihapus
    public void undoRemove() {
        if (!removedTasks.isEmpty()) {
            RemovedTask lastRemoved = removedTasks.pop();
            if (lastRemoved.prev == null) {
                lastRemoved.node.next = head;
                head = lastRemoved.node;
            } else {
                lastRemoved.node.next = lastRemoved.prev.next;
                lastRemoved.prev.next = lastRemoved.node;
            }
            System.out.println("Undo successful: Task re-added to the list at the correct position.");
        } else {
            System.out.println("No task removals to undo.");
        }
    }
}

class Node {
    String task;
    Node next;

    public Node(String task) {
        this.task = task;
    }
}
// Menyimpan informasi task yang dihapus
class RemovedTask {
    Node node;
    Node prev;
    public RemovedTask(Node node, Node prev) {
        this.node = node;
        this.prev = prev;
    }
}

// Main Class
public class DailyTaskManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskArray taskArray = new TaskArray();
        TaskStack undoStack = new TaskStack();
        TaskLinkedList taskList = new TaskLinkedList();
        
        while (true) {
            System.out.println("\u001b[38;5;147m" + "╔══════════════════════════════════════╗" + "\u001b[0m"); 
            System.out.println("\u001b[38;5;218m" + "  Welcome to Daily Task Manager (^_^)!" + "\u001b[0m");
            System.out.println("\u001b[38;5;147m" + "╚══════════════════════════════════════╝" + "\u001b[0m");
            System.out.println("\u001b[38;5;151m");
            System.out.println("1. View Task (Array)");
            System.out.println("2. Update Task (Array)");
            System.out.println("3. Undo Task Update (Array)");
            System.out.println("4. Mark Task as Completed (Push to Undo Stack)");
            System.out.println("5. Undo Last Completed Task (Pop from Stack)");
            System.out.println("6. View Undo Stack (Peek)");
            System.out.println("7. Add Task to Dynamic List");
            System.out.println("8. Remove Task from Dynamic List");
            System.out.println("9. Show all Dynamic Task List");
            System.out.println("10. Undo Task Removal (Dynamic List)");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.println("\u001b[0m");
            
            switch (choice) {
                case 1:
                    taskArray.displayTasks();
                    break;
                case 2:
                    System.out.print("Enter task index to update: ");
                    int index = scanner.nextInt() - 1;
                    scanner.nextLine();
                    System.out.print("Enter new task: ");
                    String newTask = scanner.nextLine();
                    taskArray.updateTask(index, newTask);
                    break;
                case 3:
                    taskArray.undoUpdate();
                    break;
                case 4:
                    System.out.print("Enter task to mark as completed: ");
                    String completedTask = scanner.nextLine();
                    undoStack.push(completedTask);
                    break;
                case 5:
                    System.out.println("Undone Task: " + undoStack.pop());
                    break;
                case 6:
                    System.out.println("Last Completed Task: " + undoStack.peek());
                    break;
                case 7:
                    System.out.print("Enter new task: ");
                    String task = scanner.nextLine();
                    taskList.addTask(task);
                    break;
                case 8:
                    System.out.print("Enter task to remove: ");
                    String removeTask = scanner.nextLine();
                    taskList.removeTask(removeTask);
                    break;
                case 9:
                    taskList.displayTasks();
                    break;
                case 10:
                    taskList.undoRemove();
                    break;
                case 11:
                    System.out.println("\u001b[38;5;147m" + "╔═══════════════════════╗" + "\u001b[0m"); 
                    System.out.println("\u001b[38;5;218m" + "  Have a nice day (^_^)!" + "\u001b[0m");
                    System.out.println("\u001b[38;5;147m" + "╚═══════════════════════╝" + "\u001b[0m");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
                
                
            }
        }
    }
}