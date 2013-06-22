package render;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class Tessellator
{
    private static boolean convertQuadsToTriangles = false;
    private static boolean tryVBO = false;
    private ByteBuffer byteBuffer;
    private IntBuffer intBuffer;
    private FloatBuffer floatBuffer;
    private int rawBuffer[];
    private int vertexCount;
    private double textureU;
    private double textureV;
    private int color;
    private boolean hasColor;
    private boolean hasTexture;
    private int rawBufferIndex;
    private int addedVertices;
    private int drawMode;
    public static final Tessellator instance = new Tessellator(0x200000);
    private boolean isDrawing;
    private boolean useVBO;
    private IntBuffer vertexBuffers;
    private int vboIndex;
    private int vboCount;
    private int bufferSize;

    private Tessellator(int i)
    {
        vertexCount = 0;
        hasColor = false;
        hasTexture = false;
        rawBufferIndex = 0;
        addedVertices = 0;
        isDrawing = false;
        useVBO = false;
        vboIndex = 0;
        vboCount = 10;
        bufferSize = i;
        byteBuffer = createDirectByteBuffer(i * 4);
        intBuffer = byteBuffer.asIntBuffer();
        floatBuffer = byteBuffer.asFloatBuffer();
        rawBuffer = new int[i];
        useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
        if (useVBO)
        {
            vertexBuffers = createDirectIntBuffer(vboCount);
            ARBVertexBufferObject.glGenBuffersARB(vertexBuffers);
        }
    }    

    public int draw()
    {
        if (!isDrawing)
        {
            throw new IllegalStateException("Not tesselating!");
        }
        isDrawing = false;
        if (vertexCount > 0)
        {
            intBuffer.clear();
            intBuffer.put(rawBuffer, 0, rawBufferIndex);
            byteBuffer.position(0);
            byteBuffer.limit(rawBufferIndex * 4);
            if (useVBO)
            {
                vboIndex = (vboIndex + 1) % vboCount;
                ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertexBuffers.get(vboIndex));
                ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, byteBuffer, 
                		ARBVertexBufferObject.GL_STREAM_DRAW_ARB);
            }
            if (hasTexture)
            {
                if (useVBO)
                {
                    GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12L);
                }
                else
                {
                    floatBuffer.position(3);
                    GL11.glTexCoordPointer(2, 32, floatBuffer);
                }
                GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            }
            
            if (hasColor)
            {
                if (useVBO)
                {
                    GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, 20L);
                }
                else
                {
                    byteBuffer.position(20);
                    GL11.glColorPointer(4, true, 32, byteBuffer);
                }
                GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            }
            if (useVBO)
            {
                GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0L);
            }
            else
            {
                floatBuffer.position(0);
                GL11.glVertexPointer(3, 32, floatBuffer);
            }
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            if (drawMode == 7 && convertQuadsToTriangles)
            {
                GL11.glDrawArrays(4, 0, vertexCount);
            }
            else
            {
                GL11.glDrawArrays(drawMode, 0, vertexCount);
            }
            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            if (hasTexture)
            {
                GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            }
            if (hasColor)
            {
                GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
            }
        }
        int i = rawBufferIndex * 4;
        reset();
        return i;
    }

    private void reset()
    {
        vertexCount = 0;
        byteBuffer.clear();
        rawBufferIndex = 0;
        addedVertices = 0;
    }

    public void startDrawingQuads()
    {
        startDrawing(GL11.GL_QUADS);
    }

    public void startDrawing(int i)
    {
        if (isDrawing)
        {
            throw new IllegalStateException("Already tesselating!");
        }
        else
        {
            isDrawing = true;
            reset();
            drawMode = i;
            hasColor = false;
            hasTexture = false;
            return;
        }
    }

    private void setTextureUV(double d, double d1)
    {
        hasTexture = true;
        textureU = d;
        textureV = d1;
    }

    public void setColorRGBA_F(float f, float f1, float f2, float f3)
    {
        setColorRGBA((int)(f * 255F), (int)(f1 * 255F), (int)(f2 * 255F), (int)(f3 * 255F));
    }

    public void setColorRGBA(int i, int j, int k, int l)
    {
        if (i > 255)
        {
            i = 255;
        }
        if (j > 255)
        {
            j = 255;
        }
        if (k > 255)
        {
            k = 255;
        }
        if (l > 255)
        {
            l = 255;
        }
        if (i < 0)
        {
            i = 0;
        }
        if (j < 0)
        {
            j = 0;
        }
        if (k < 0)
        {
            k = 0;
        }
        if (l < 0)
        {
            l = 0;
        }
        hasColor = true;
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            color = l << 24 | k << 16 | j << 8 | i;
        }
        else
        {
            color = i << 24 | j << 16 | k << 8 | l;
        }
    }

    public void addVertexWithUV(double d, double d1, double d2, double d3, double d4)
    {
        setTextureUV(d3, d4);
        addVertex(d, d1, d2);
    }

    private void addVertex(double d, double d1, double d2)
    {
        addedVertices++;
        if (drawMode == 7 && convertQuadsToTriangles && addedVertices % 4 == 0)
        {
            for (int i = 0; i < 2; i++)
            {
                int j = 8 * (3 - i);
                if (hasTexture)
                {
                    rawBuffer[rawBufferIndex + 3] = rawBuffer[(rawBufferIndex - j) + 3];
                    rawBuffer[rawBufferIndex + 4] = rawBuffer[(rawBufferIndex - j) + 4];
                }               
                if (hasColor)
                {
                    rawBuffer[rawBufferIndex + 5] = rawBuffer[(rawBufferIndex - j) + 5];
                }
                rawBuffer[rawBufferIndex + 0] = rawBuffer[(rawBufferIndex - j) + 0];
                rawBuffer[rawBufferIndex + 1] = rawBuffer[(rawBufferIndex - j) + 1];
                rawBuffer[rawBufferIndex + 2] = rawBuffer[(rawBufferIndex - j) + 2];
                vertexCount++;
                rawBufferIndex += 8;
            }
        }
        if (hasTexture)
        {
            rawBuffer[rawBufferIndex + 3] = Float.floatToRawIntBits((float)textureU);
            rawBuffer[rawBufferIndex + 4] = Float.floatToRawIntBits((float)textureV);
        }
        if (hasColor)
        {
            rawBuffer[rawBufferIndex + 5] = color;
        }
        rawBuffer[rawBufferIndex + 0] = Float.floatToRawIntBits((float)(d));
        rawBuffer[rawBufferIndex + 1] = Float.floatToRawIntBits((float)(d1));
        rawBuffer[rawBufferIndex + 2] = Float.floatToRawIntBits((float)(d2));
        rawBufferIndex += 8;
        vertexCount++;
        if (vertexCount % 4 == 0 && rawBufferIndex >= bufferSize - 32)
        {
            draw();
            isDrawing = true;
        }
    }
    
    private static synchronized ByteBuffer createDirectByteBuffer(int i)
    {
        ByteBuffer bytebuffer = ByteBuffer.allocateDirect(i).order(ByteOrder.nativeOrder());
        return bytebuffer;
    }

    private static IntBuffer createDirectIntBuffer(int i)
    {
        return createDirectByteBuffer(i << 2).asIntBuffer();
    }
}