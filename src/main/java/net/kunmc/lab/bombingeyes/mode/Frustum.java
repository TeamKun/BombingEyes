package net.kunmc.lab.bombingeyes.mode;

import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

public class Frustum {
    private Vector left_bottom_near;
    private Vector right_bottom_near;
    private Vector left_top_near;
    private Vector right_top_near;

    private Vector left_bottom_far;
    private Vector right_bottom_far;
    private Vector left_top_far;
    private Vector right_top_far;

    double fov;
    double aspectRatio;
    double nearPlane;
    double farPlane;

    Frustum(double FOV,double aspectRatio, double NEAR_PLANE,double FAR_PLANE) {

        this.fov = FOV;
        this.aspectRatio = aspectRatio;
        this.nearPlane = NEAR_PLANE;
        this.farPlane = FAR_PLANE;

        double y_scale = 1.0f / (float)Math.tan( Math.toRadians(FOV / 2.0) );
        double x_scale = y_scale * aspectRatio;

        double near_x = NEAR_PLANE * x_scale;
        double near_y = NEAR_PLANE * y_scale;

        double far_x = FAR_PLANE * x_scale;
        double far_y = FAR_PLANE * y_scale;

        this.left_bottom_near  = new Vector(-near_x, -near_y, NEAR_PLANE);
        this.right_bottom_near = new Vector( near_x, -near_y, NEAR_PLANE);
        this.left_top_near     = new Vector(-near_x,  near_y, NEAR_PLANE);
        this.right_top_near    = new Vector( near_x,  near_y, NEAR_PLANE);

        this.left_bottom_far  = new Vector(-far_x, -far_y, FAR_PLANE);
        this.right_bottom_far = new Vector( far_x, -far_y, FAR_PLANE);
        this.left_top_far     = new Vector(-far_x,  far_y, FAR_PLANE);
        this.right_top_far    = new Vector( far_x,  far_y, FAR_PLANE);
    }

    Frustum(Vector left_bottom_near,
            Vector right_bottom_near,
            Vector left_top_near,
            Vector right_top_near,
            Vector left_bottom_far,
            Vector right_bottom_far,
            Vector left_top_far,
            Vector right_top_far) {

        this.left_bottom_near  = left_bottom_near;
        this.right_bottom_near = right_bottom_near;
        this.left_top_near     = left_top_near;
        this.right_top_near    = right_top_near;

        this.left_bottom_far  =  left_bottom_far;
        this.right_bottom_far =  right_bottom_far;
        this.left_top_far     =  left_top_far;
        this.right_top_far    =  right_top_far;
    }

    /**
     * 平行移動する
     * @param v
     * @return
     */
    public Frustum translate(Vector v) {
        return new Frustum(
                this.left_bottom_near.add(v),
                this.right_bottom_near.add(v),
                this.left_top_near.add(v),
                this.right_top_near.add(v),
                this.left_bottom_far.add(v),
                this.right_bottom_far.add(v),
                this.left_top_far.add(v),
                this.right_top_far.add(v));
    }

    /**
     * ヨーの回転を加える
     * @param yaw
     * @return
     */
    public Frustum rotateYaw(double yaw) {
        double rad = Math.toRadians(yaw);
        return new Frustum(
                calcRotate(this.left_bottom_near,rad),
                calcRotate(this.right_bottom_near,rad),
                calcRotate(this.left_top_near,rad),
                calcRotate(this.right_top_near,rad),
                calcRotate(this.left_bottom_far,rad),
                calcRotate(this.right_bottom_far,rad),
                calcRotate(this.left_top_far,rad),
                calcRotate(this.right_top_far,rad));
    }

    /**
     * ピッチの回転を加える
     * @param pitch
     * @return
     */
    public Frustum rotatePitch(double pitch) {

        double rad = Math.toRadians(pitch);
        double sinY = Math.sin(rad);
        double cosY = Math.cos(rad);

        double[][] matrix = new double[3][3];
        matrix[0][0] = 1;
        matrix[0][1] = 0;
        matrix[0][2] = 0;
        matrix[1][0] = 0;
        matrix[1][1] = cosY;
        matrix[1][2] = -sinY;
        matrix[2][0] = 0;
        matrix[2][1] = sinY;
        matrix[2][2] = cosY;

        return new Frustum(calcMatrix(matrix,left_bottom_near),
                calcMatrix(matrix,right_bottom_near),
                calcMatrix(matrix,left_top_near),
                calcMatrix(matrix,right_top_near),
                calcMatrix(matrix,left_bottom_far),
                calcMatrix(matrix,right_bottom_far),
                calcMatrix(matrix,left_top_far),
                calcMatrix(matrix,right_top_far));
    }

    /**
     * 回転を計算する
     * @param
     */
    private Vector calcRotate(Vector v,double r) {

        double x = v.getX();
        double z =  v.getZ();

        double cos = Math.cos(r);
        double sin = Math.sin(r);

        return new Vector((cos * x - sin * z),v.getY(),sin * x + cos * z);
    }



    public Vector calcMatrix(double[][] m,Vector v) {
        double x = v.getX();
        double y = v.getY();
        double z = v.getZ();
        return new Vector(m[0][0] * x + m[0][1] * y + m[0][2] * z,
                m[1][0] * x + m[1][1] * y + m[1][2] * z,
                m[2][0] * x + m[2][1] * y + m[2][2] * z);
    }

    public boolean isInSight(Vector target) {

        // 右面の判定
        if (-calcDot(right_bottom_far,right_top_far,right_bottom_near,target)<1) {
            return false;
        }
        // 左面の判定
        if (calcDot(left_bottom_far,left_top_far,left_bottom_near,target)<1) {
            return false;
        }
        // 上面の判定
        if (calcDot(left_top_far,right_top_far,left_top_near,target)<1) {
            return false;
        }
        // 下面の判定
        if (-calcDot(left_bottom_far,right_bottom_far,left_bottom_near,target)<1) {
            return false;
        }

        // ニアプレーンの判定
        if (-calcDot(left_top_near,left_bottom_near,right_bottom_near,target)<1) {
            return false;
        }
        // ファープレーンの判定
        if (calcDot(left_top_far,left_bottom_far,right_bottom_far,target)<1) {
            return false;
        }
        return true;

    }

    private double calcDot(Vector a, Vector b, Vector c, Vector target) {
        Vector newA = a.clone();
        Vector newB = b.clone();
        Vector newC = c.clone();
        Vector newTarget = target.clone();

        Vector u = newA.subtract(newC);
        Vector v = newB.subtract(newC);
        Vector normal = v.crossProduct(u);
        Vector tc = newTarget.subtract(newC);

        return tc.dot(normal);
    }
}
