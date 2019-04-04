package constraints;

import java.awt.Graphics2D;

public abstract class Drawable {
//Render method if page contains drawables	
public abstract void render(Graphics2D g);
//tick method for logic update
public abstract void tick();


}
