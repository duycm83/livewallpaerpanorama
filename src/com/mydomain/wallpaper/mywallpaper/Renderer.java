package com.mydomain.wallpaper.mywallpaper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.RotateAnimation3D;
import rajawali.animation.TranslateAnimation3D;
import rajawali.curves.CatmullRomCurve3D;
import rajawali.lights.ALight;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.methods.DiffuseMethod;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
import rajawali.math.vector.Vector3;
import rajawali.primitives.Cube;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;

public class Renderer extends RajawaliRenderer {
	private TranslateAnimation3D mCamAnim;
	
	public Renderer(Context context) {
		super(context);
	}

	public void initScene() {
		getCurrentCamera().setFarPlane(1000);
		/*
		 * Skybox images by Emil Persson, aka Humus. http://www.humus.name humus@comhem.se
		 */
		try {
			getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx,
					R.drawable.posy, R.drawable.negy, R.drawable.posz,
					R.drawable.negz);
		} catch (TextureException e) {
			e.printStackTrace();
		}
		ALight light = new DirectionalLight(-1, 0, -1);
		light.setPower(2);
		
		getCurrentScene().addLight(light);
		getCurrentCamera().setPosition(0, 0, 7);
		getCurrentCamera().setLookAt(0, 0, 0);

		try {
			Cube cube = new Cube(1);
			Material material = new Material();
			material.enableLighting(true);
			material.setDiffuseMethod(new DiffuseMethod.Lambert());
			material.addTexture(new Texture("rajawaliTex", R.drawable.img01));
			material.setColorInfluence(0);
			cube.setMaterial(material);
			addChild(cube);

			Vector3 axis = new Vector3(3, 1, 6);
			axis.normalize();
			RotateAnimation3D anim = new RotateAnimation3D(axis, 360);
			anim.setDuration(8000);
			anim.setRepeatMode(RepeatMode.INFINITE);
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			anim.setTransformable3D(cube);
			registerAnimation(anim);
			anim.play();
			
			CatmullRomCurve3D path = new CatmullRomCurve3D();
			path.addPoint(new Vector3(-4, 0, -20));
			path.addPoint(new Vector3(2, 1, -10));
			path.addPoint(new Vector3(-2, 0, 10));
			path.addPoint(new Vector3(0, -4, 20));
			path.addPoint(new Vector3(5, 10, 30));
			path.addPoint(new Vector3(-2, 5, 40));
			path.addPoint(new Vector3(3, -1, 60));
			path.addPoint(new Vector3(5, -1, 70));
			
			
			// -- inner ring

//			float radius = .8f;
//			int count = 0;
//
//			for (int i = 0; i < 360; i += 36) {
//				double radians = MathUtil.degreesToRadians(i);
//				int color = 0xfed14f;
//				if (count % 3 == 0)
//					color = 0x10a962;
//				else if (count % 3 == 1)
//					color = 0x4184fa;
//				count++;
//				float x =(float) Math.sin(radians) * radius;
//				float y =0;
//				float z =(float) Math.cos(radians) * radius;
//				Vector3 v = new Vector3(x, y, z);
//				path.addPoint(v);
//			}
			
			mCamAnim = new TranslateAnimation3D(path);
			mCamAnim.setDuration(20000);
			mCamAnim.setRepeatMode(RepeatMode.REVERSE_INFINITE);
			mCamAnim.setTransformable3D(getCurrentCamera());
			mCamAnim.setInterpolator(new AccelerateDecelerateInterpolator());
			registerAnimation(mCamAnim);
			mCamAnim.play();

		} catch (TextureException e) {
			e.printStackTrace();
		}
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
	}

	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		getCurrentCamera().setRotY(getCurrentCamera().getRotY() - .2f);
	}
}
