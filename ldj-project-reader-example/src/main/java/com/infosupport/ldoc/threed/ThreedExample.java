package com.infosupport.ldoc.threed;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.GL.GL_POINTS;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import com.infosupport.ldoc.reader.Project;
import com.infosupport.ldoc.reader.impl.JacksonProjectFactory;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.alg.drawing.FRLayoutAlgorithm2D;
import org.jgrapht.alg.drawing.RescaleLayoutAlgorithm2D;
import org.jgrapht.alg.drawing.model.Box2D;
import org.jgrapht.alg.drawing.model.MapLayoutModel2D;
import org.jgrapht.alg.drawing.model.Point2D;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class ThreedExample implements Runnable, GLEventListener {

  private final Project project;

  private GL2 gl;
  private GLU glu;
  private GLUT glut;
  private FPSAnimator animator;
  private MapLayoutModel2D<String> layout;
  private Graph<String, DefaultEdge> graph;
  private final Random rnd = new Random(0);
  private int mouseX;
  private boolean mouseAway = false;

  public ThreedExample(Project project) {
    this.project = project;
  }

  @Override
  public void init(GLAutoDrawable drawable) {
    gl = drawable.getGL().getGL2();
    glu = new GLU();
    glut = new GLUT();
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
  }

  @Override
  public void display(GLAutoDrawable drawable) {
    gl.glClearColor(.1f, .1f, .1f, 0f);
    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    gl.glMatrixMode(GL_MODELVIEW);
    gl.glLoadIdentity();
    glu.gluLookAt(/* eye */ 0, 10, 7.5, /* center */ 0, 0, 0, /* up */ 0, 0, 1);

    if (mouseAway) {
      mouseX += 1;
      mouseX %= drawable.getSurfaceWidth();
    }

    gl.glRotated((double) mouseX / drawable.getSurfaceWidth() * 360.0, 0, 0, 1);

    /* Stars */
    rnd.setSeed(0);
    double sf = 50;
    gl.glPointSize(3);
    gl.glBegin(GL_POINTS);
    for (int i = 0; i < 200; i++) {
      double brightness = rnd.nextDouble() + .1;
      gl.glColor3d(brightness, brightness, brightness);
      gl.glVertex3d(rnd.nextDouble(-sf, sf), rnd.nextDouble(-sf, sf), rnd.nextDouble(-sf, 0));
    }
    gl.glEnd();

    /* Grid */
    gl.glColor3d(0.3, 0.3, 0.3);
    gl.glBegin(GL_LINES);
    for (int row = 0; row <= 12; ++row) {
      gl.glVertex3d(-6, -6 + row, 0);
      gl.glVertex3d(6, -6 + row, 0);
    }
    for (int col = 0; col <= 12; ++col) {
      gl.glVertex3d(-6 + col, -6, 0);
      gl.glVertex3d(-6 + col, 6, 0);
    }
    gl.glEnd();

    /* Labels and edges */
    for (String vertex : graph.vertexSet()) {
      Point2D from = layout.get(vertex);

      gl.glColor3d(1, 1, 1);
      for (DefaultEdge edge : graph.outgoingEdgesOf(vertex)) {
        Point2D to = layout.get(graph.getEdgeTarget(edge));
        gl.glBegin(GL_LINES);
        gl.glVertex3d(from.getX(), from.getY(), 0.0);
        gl.glVertex3d(to.getX(), to.getY(), 0.0);
        gl.glEnd();
      }

      gl.glRasterPos3d(from.getX(), from.getY(), 0.0);
      glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, vertex.replaceAll("^.*\\.", ""));
    }

    gl.glFlush();
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    gl.glMatrixMode(GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluPerspective(45, (double) width / (float) height, 5, 100);
  }

  @Override
  public void run() {
    graph = new DefaultUndirectedGraph<>(DefaultEdge.class);

    project.interfaces().forEach(t -> {
      graph.addVertex(t.fullName());

      if (t.basetypes() != null) {
        t.basetypes().forEach(bt -> {
          graph.addVertex(bt);
          graph.addEdge(t.fullName(), bt);
        });
      }
    });

    for (String vtx : Set.copyOf(graph.vertexSet())) {
      if (graph.degreeOf(vtx) == 0) {
        graph.removeVertex(vtx);
      }
    }

    layout = new MapLayoutModel2D<>(new Box2D(-6, -6, 12, 12));
    new FRLayoutAlgorithm2D<String, DefaultEdge>().layout(graph, layout);
    new RescaleLayoutAlgorithm2D<String, DefaultEdge>(6).layout(graph, layout);

    var glWindow = GLWindow.create(new GLCapabilities(null));

    glWindow.addGLEventListener(this);
    glWindow.setWindowDestroyNotifyAction(() -> animator.stop());
    glWindow.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          animator.stop();
        }
      }
    });
    glWindow.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        mouseAway = !mouseAway;
      }

      @Override
      public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
      }
    });
    glWindow.setVisible(true);

    animator = new FPSAnimator(glWindow, 60);
    animator.start();
  }

  public static void main(String[] args) throws IOException {
    System.setProperty("jogl.disable.openglcore", "true");
    System.setProperty("sun.awt.noerasebackground", "true");
    new ThreedExample(new JacksonProjectFactory().project(new File(args[0]))).run();
  }
}
