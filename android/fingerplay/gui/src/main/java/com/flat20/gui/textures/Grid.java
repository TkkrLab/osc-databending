package com.flat20.gui.textures;


/**
 * A 2D rectangular mesh. Can be drawn textured or untextured.
 * This version is modified from the original Grid.java (found in
 * the SpriteText package in the APIDemos Android sample) to support hardware
 * vertex buffers.
 */
public abstract class Grid extends Mesh {
/*
	protected boolean mNeedsVertexBufferUpdate = false;
	protected boolean mNeedsTexCoordBufferUpdate = false;

    protected ByteBuffer mVertexBufferByte;
    protected FloatBuffer mVertexBuffer;
    private int mVertexBufferSize;
    //protected float[] mVertexArray;

    protected ByteBuffer mTexCoordBufferByte;
    protected FloatBuffer mTexCoordBuffer;
    private int mTexCoordBufferSize;
    //protected float[] mTexCoordArray;

    private CharBuffer mIndexBuffer;
*/
    private int mW;
    private int mH;
/*
    public int mIndexCount;
    public int mVertBufferIndex;
    public int mIndexBufferIndex;
    public int mTextureCoordBufferIndex;
*/
    //private GL11 tempGl;

    protected Grid(int vertsAcross, int vertsDown) {
        if (vertsAcross < 0 || vertsAcross >= 65536) {
            throw new IllegalArgumentException("vertsAcross");
        }
        if (vertsDown < 0 || vertsDown >= 65536) {
            throw new IllegalArgumentException("vertsDown");
        }
        if (vertsAcross * vertsDown >= 65536) {
            throw new IllegalArgumentException("vertsAcross * vertsDown >= 65536");
        }

        mW = vertsAcross;
        mH = vertsDown;
        int size = vertsAcross * vertsDown;
/*        
        mVertexBufferSize = size * 3 * 4;
        mVertexBufferByte = ByteBuffer.allocateDirect(mVertexBufferSize).order(ByteOrder.nativeOrder());
        mVertexBuffer = mVertexBufferByte.asFloatBuffer();

        mTexCoordBufferSize = size * 2 * 4;
        mTexCoordBufferByte = ByteBuffer.allocateDirect(mTexCoordBufferSize).order(ByteOrder.nativeOrder());
        mTexCoordBuffer = mTexCoordBufferByte.asFloatBuffer();
*/

        
        int quadW = mW - 1;
        int quadH = mH - 1;
        int quadCount = quadW * quadH;
        int indexCount = quadCount * 6;
        //mIndexCount = indexCount;
        char[] indexArray = new char[indexCount];

        /*
         * Initialize triangle list mesh.
         *
         *     [0]-----[  1] ...
         *      |    /   |
         *      |   /    |
         *      |  /     |
         *     [w]-----[w+1] ...
         *      |       |
         *
         */

        {
            int i = 0;
            for (int y = 0; y < quadH; y++) {
                for (int x = 0; x < quadW; x++) {
                    char a = (char) (y * mW + x);//0,1
                    char b = (char) (y * mW + x + 1);//1,2
                    char c = (char) ((y + 1) * mW + x);//1,2
                    char d = (char) ((y + 1) * mW + x + 1);//2,3

                    indexArray[i++] = a;//0
                    indexArray[i++] = b;//1
                    indexArray[i++] = c;//1

                    indexArray[i++] = b;//1
                    indexArray[i++] = c;//1
                    indexArray[i++] = d;//2
                }
            }
        }
/*
        //mIndexBuffer = CharBuffer.wrap(indexArray);
        mIndexBuffer = ByteBuffer.allocateDirect( indexCount*2 ).order(ByteOrder.nativeOrder()).asCharBuffer();
        mIndexBuffer.put( indexArray );
        mIndexBuffer.position(0);

        mVertBufferIndex = 0;
*/
        
        createBuffers(size, indexArray);
    }

/*
	public void setSize(int width, int height) {
		Log.e("Grid", "override setSize");
	}
*/
    // i = x index
    // j = y index
    public void set(int i, int j, float x, float y, float z, float u, float v) {
        if (i < 0 || i >= mW) {
            throw new IllegalArgumentException("i");
        }
        if (j < 0 || j >= mH) {
            throw new IllegalArgumentException("j");
        }

        int index = mW * j + i;
        
        super.set(index, x, y, z, u, v);
/*
        int posIndex = index * 3;

        mVertexBuffer.put(posIndex, x);
        mVertexBuffer.put(posIndex+1, y);
        mVertexBuffer.put(posIndex+2, z);
        //mVertexArray[posIndex] = x;
        //mVertexArray[posIndex + 1] = y;
        //mVertexArray[posIndex + 2] = z;

        int texIndex = index * 2;
        mTexCoordBuffer.put(texIndex, u);
        mTexCoordBuffer.put(texIndex+1, v);
        //mTexCoordArray[texIndex] = u;
        //mTexCoordArray[texIndex + 1] = v;
*/

    }
/*
    // Sends vertices to GPU
    private void updateVertexBuffer(GL11 gl) {
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, mVertexBufferSize, mVertexBufferByte, GL11.GL_STATIC_DRAW);
    }

    private void updateTextureCoordBuffer(GL11 gl) {
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, mTexCoordBufferSize, mTexCoordBufferByte, GL11.GL_STATIC_DRAW);
    }

*/
/*
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
*/
    /** 
     * When the OpenGL ES device is lost, GL handles become invalidated.
     * In that case, we just want to "forget" the old handles (without
     * explicitly deleting them) and make new ones.
     */
    /*
    public void forgetHardwareBuffers() {
        mVertBufferIndex = 0;
        mIndexBufferIndex = 0;
        mTextureCoordBufferIndex = 0;
        //tempGl = null;
    }*/

    /**
     * Deletes the hardware buffers allocated by this object (if any).
     */
    /*
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
    }*/
    
    /** 
     * Allocates hardware buffers on the graphics card and fills them with
     * data if a buffer has not already been previously allocated.  Note that
     * this function uses the GL_OES_vertex_buffer_object extension, which is
     * not guaranteed to be supported on every device.
     * @param gl  A pointer to the OpenGL ES context.
     */
    /*
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
    }*/
    
    
}
