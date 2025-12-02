package CreatureZoo;

public class Animate<E extends Creature> implements Runnable{
    private Rasterizer<E> raster;
    private boolean running;

    public Animate (Rasterizer<E> raster) {
        this.raster = raster;
        this.running = true;
    }
    public void setRaster(Rasterizer<E> raster) {
        this.raster = raster;
    }
    public Rasterizer<E> getRaster() {
        return this.raster;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
    public boolean getRunning() {
        return this.running;
    }
    
    @Override
    public void run() {
        while (running) {
            if (raster.getRast().isEmpty()) {
                raster.update();
            }
            else {
                for (E e : raster.getRast()) {
                    e.circle();
                }
                raster.update(); 
            }           
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }
    }
}
