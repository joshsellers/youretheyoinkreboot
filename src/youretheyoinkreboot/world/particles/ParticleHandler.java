package youretheyoinkreboot.world.particles;

import youretheyoinkreboot.core.gfx.Screen;
import youretheyoinkreboot.world.World;

/**
 *
 * @author josh
 */
public class ParticleHandler {
    public static final int MAX_PARTICLES = 512;
    
    private final Particle[] particles = new Particle[MAX_PARTICLES];
    
    private World w;
    
    private int numActive = 0;
    
    public ParticleHandler(World w) {
        this.w = w;
        for (int i = 0; i < MAX_PARTICLES; i++) {
            particles[i] = new Particle(Particle.PLACEHOLDER_PARTICLE, 0, 0, 0, 0, w) {
                @Override
                protected void tick() {}
                @Override
                protected void render(Screen s) {}
            };
        }
    }
    
    public void tick() {
        int active = 0;
        for (Particle p : getParticles()) {
            if (p.isActive() && p.getID() != Particle.PLACEHOLDER_PARTICLE) {
                p.superTick();
                active++;
            }
        }
        numActive = active;
    }
    
    public void render(Screen s) {
        for (Particle p : getParticles()) {
            if (p.isVisible() && p.isActive() && p.getID() != Particle.PLACEHOLDER_PARTICLE) p.superRender(s);
        }
    }
    
    public void addParticle(Particle p) {
        for (int i = 0; i < MAX_PARTICLES; i++) {
            if (getParticles()[i].getID() == Particle.PLACEHOLDER_PARTICLE || !getParticles()[i].isActive()) {
                particles[i] = p;
                particles[i].decay();
                return;
            }
        }
        particles[MAX_PARTICLES-1] = p;
        p.decay();
    }
    
    public Particle[] getParticles() {
        return particles;
    }
    
    public int getParticleCount() {
        return numActive;
    }
}
