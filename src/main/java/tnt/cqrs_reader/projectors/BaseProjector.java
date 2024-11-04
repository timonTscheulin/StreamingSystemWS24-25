package tnt.cqrs_reader.projectors;

public abstract class BaseProjector implements Runnable {
    public abstract void project();

    @Override
    public void run() {
        // Die Logik für das kontinuierliche Lesen und Verarbeiten der Events
        while (true) {
            project();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
