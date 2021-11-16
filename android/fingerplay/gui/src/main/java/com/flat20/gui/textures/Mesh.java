package com.flat20.gui.textures;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.util.Log;

public abstract class Mesh {

	protected boolean mNeedsVertexBufferUpdate = false;
	protected boolean mNeedsTexCoordBufferUpdate = false;

	// Preferably these would all be private but a lot of old subclasses
	// are using texcoordbuffer and vertexbuffer directly in their setSize().
	// TODO Go through all subclasses and fix this.
	
    private ByteBuffer mVertexBufferByte;
    protected FloatBuffer mVertexBuffer;
    private int mVertexBufferSize;

    protected ByteBuffer mTexCoordBufferByte;
    protected FloatBuffer mTexCoordBuffer;
    private int mTexCoordBufferSize;

    private CharBuffer mIndexBuffer;

    public int mIndexCount;
    private int mVertBufferIndex;
    private int mIndexBufferIndex;
    private int mTextureCoordBufferIndex;

    protected Mesh() {

        mVertBufferIndex = 0;
    }

    protected void createBuffers(int vertexCount, char[] indexArray) {

    	mVertexBufferSize = vertexCount * 3 * (Float.SIZE/8); // 4 = sizeof float?
        mVertexBufferByte = ByteBuffer.allocateDirect(mVertexBufferSize).order(ByteOrder.nativeOrder());
        mVertexBuffer = mVertexBufferByte.asFloatBuffer();

        mTexCoordBufferSize = vertexCount * 2 * (Float.SIZE/8);
        mTexCoordBufferByte = ByteBuffer.allocateDirect(mTexCoordBufferSize).order(ByteOrder.nativeOrder());
        mTexCoordBuffer = mTexCoordBufferByte.asFloatBuffer();

        mIndexCount = indexArray.length;
    	mIndexBuffer = ByteBuffer.allocateDirect( mIndexCount*2 ).order(ByteOrder.nativeOrder()).asCharBuffer();
        mIndexBuffer.put( indexArray );
        mIndexBuffer.position(0);
        
        
    }


	public void setSize(int width, int height) {
		Log.e("Mesh", "override setSize");
	}

	final public void set(int index, float x, float y, float z, float u, float v) {

		final int posIndex = index * 3;
        mVertexBuffer.put(posIndex, x);
        mVertexBuffer.put(posIndex+1, y);
        mVertexBuffer.put(posIndex+2, z);

        final int texIndex = index * 2;
        mTexCoordBuffer.put(texIndex, u);
        mTexCoordBuffer.put(texIndex+1, v);
    }

    // Sends vertices to GPU
    final private void updateVertexBuffer(GL11 gl) {
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, mVertexBufferSize, mVertexBufferByte, GL11.GL_STATIC_DRAW);
    }

    final private void updateTextureCoordBuffer(GL11 gl) {
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, mTexCoordBufferSize, mTexCoordBufferByte, GL11.GL_STATIC_DRAW);
    }



    public void draw(GL10 gl) {
        final GL11 gl11 = (GL11)gl;

        if (mNeedsVertexBufferUpdate) {
        	updateVertexBuffer(gl11);
        	mNeedsVertexBufferUpdate = false;
        }

        if (mNeedsTexCoordBufferUpdate) {
        	updateTextureCoordBuffer(gl11);
        	mNeedsTexCoordBufferUpdate = false;
        }

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
        gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);

        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
        gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);

        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferIndex);
        gl11.glDrawElements(GL11.GL_TRIANGLES, mIndexCount, GL11.GL_UNSIGNED_SHORT, 0);

        // Are these needed?
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    /** 
     * When the OpenGL ES device is lost, GL handles become invalidated.
     * In that case, we just want to "forget" the old handles (without
     * explicitly deleting them) and make new ones.
     */
    public void forgetHardwareBuffers() {
        mVertBufferIndex = 0;
        mIndexBufferIndex = 0;
        mTextureCoordBufferIndex = 0;
        //tempGl = null;
    }

    /**
     * Deletes the hardware buffers allocated by this object (if any).
     */
    public void freeHardwareBuffers(GL10 gl) {
        if (mVertBufferIndex != 0) {
            if (gl instanceof GL11) {
                GL11 gl11 = (GL11)gl;
                int[] buffer = new int[1];
                buffer[0] = mVertBufferIndex;
                gl11.glDeleteBuffers(1, buffer, 0);
                
                buffer[0] = mTextureCoordBufferIndex;
                gl11.glDeleteBuffers(1, buffer, 0);
                
                buffer[0] = mIndexBufferIndex;
                gl11.glDeleteBuffers(1, buffer, 0);
            }
            
            forgetHardwareBuffers();
        }
    }
    
    /** 
     * Allocates hardware buffers on the graphics card and fills them with
     * data if a buffer has not already been previously allocated.  Note that
     * this function uses the GL_OES_vertex_buffer_object extension, which is
     * not guaranteed to be supported on every device.
     * @param gl  A pointer to the OpenGL ES context.
     */
    public void generateHardwareBuffers(GL10 gl) {
        if (mVertBufferIndex == 0) {
            if (gl instanceof GL11) {

                final GL11 gl11 = (GL11)gl;
                final int[] buffer = new int[1];

                // Allocate and fill the vertex buffer.
                gl11.glGenBuffers(1, buffer, 0);
                mVertBufferIndex = buffer[0];
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
                //final int vertexSize = mVertexBufferByte.capacity();// * Float.SIZE; 
                //ByteBuffer bla = ByteBuffer.allocateDirect(vertexSize);
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, mVertexBufferSize, mVertexBufferByte, GL11.GL_STATIC_DRAW);

                
                // Allocate and fill the texture coordinate buffer.
                gl11.glGenBuffers(1, buffer, 0);
                mTextureCoordBufferIndex = buffer[0];
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
                //final int texCoordSize = mTexCoordBuffer.capacity() * Float.SIZE;
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, mTexCoordBufferSize, mTexCoordBufferByte, GL11.GL_STATIC_DRAW);    

                // Unbind the array buffer.
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

                // Allocate and fill the index buffer.
                gl11.glGenBuffers(1, buffer, 0);
                mIndexBufferIndex = buffer[0];
                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferIndex);
                // A char is 2 bytes.
                gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexCount<<1, mIndexBuffer, GL11.GL_STATIC_DRAW);

                // Unbind the element array buffer.
                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);                
            }
        }
    }

}
