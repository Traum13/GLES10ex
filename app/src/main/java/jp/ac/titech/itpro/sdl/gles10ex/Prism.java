package jp.ac.titech.itpro.sdl.gles10ex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kakaminaoto on 2016/05/27.
 */
public class Prism implements SimpleRenderer.Obj {

    private FloatBuffer vbuf;
    private float x, y, z;
    private int nPlane;

    public Prism(float s, float x, float y, float z, int nPlane) {
        ArrayList<Float> verticesList = new ArrayList<>();
        ArrayList<Float> coss = new ArrayList<>();
        ArrayList<Float> sins = new ArrayList<>();

        for(int i=0;i<nPlane;i++){
            float x1 = (float)(s * Math.cos(2 * Math.PI / nPlane * i));
            float y1 = (float)(s * Math.sin(2 * Math.PI / nPlane * i));
            coss.add(x1);
            sins.add(y1);
        }

        //bottom
        for(int i=0;i<nPlane;i++){
            Collections.addAll(verticesList, new Float[]{coss.get(i), sins.get(i), 0f});
        }

        //top
        for(int i=0;i<nPlane;i++){
            Collections.addAll(verticesList, new Float[]{coss.get(i), sins.get(i), s});
        }

        //side
        for(int i=0;i<nPlane;i++){
            float x1 = coss.get(i);
            float y1 = sins.get(i);
            float x2 = coss.get((i+1)%nPlane);
            float y2 = sins.get((i+1)%nPlane);
            float z1 = 0;
            float z2 = s;

            Collections.addAll(verticesList, new Float[]{
                    x1,y1,z1,
                    x2,y2,z1,
                    x1,y1,z2,
                    x2,y2,z2
            });
        }
        float[] vertices = new float[verticesList.size()];
        for(int i=0;i<vertices.length;i++){
            vertices[i] = verticesList.get(i);
        }

        vbuf = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vbuf.put(vertices);
        vbuf.position(0);
        this.x = x;
        this.y = y;
        this.z = z;
        this.nPlane = nPlane;
    }

    @Override
    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vbuf);
        ArrayList<Float> coss = new ArrayList<>();
        ArrayList<Float> sins = new ArrayList<>();

        // bottom
        gl.glNormal3f(0, 0, -1);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, nPlane);

        // top
        gl.glNormal3f(0, 0, 1);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, nPlane, nPlane);

        //side
        for(int i=0;i<nPlane;i++){
            float x1 = (float)(Math.cos(2 * Math.PI / nPlane * (i+0.5)));
            float y1 = (float)(Math.sin(2 * Math.PI / nPlane * (i+0.5)));
            gl.glNormal3f(x1, y1, 0);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 2 * nPlane + 4 * i, 4);
        }
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getZ() {
        return z;
    }
}
