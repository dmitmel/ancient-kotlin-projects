/*
 * Copyright (c) 2017 FedOfCoders.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import org.joml.*;
import org.fedofcoders.gamefx.*;
import org.fedofcoders.gamefx.math.*;

import java.lang.Math;

public class GameFXTest {
    public static void main(String[] args) {
//        GameObject gameObject = new GameObject("GameObject");
//        gameObject.lookAt(new Vector3f(1, 0, 0), new Vector3f(0, 1, 0));
//        Quaternionf rotation = gameObject.getRotation();
//        Vector3f result = rotation.transform(new Vector3f(0, 0, 1));
//        System.out.printf("%f %f %f", result.x, result.y, result.z);
    
        System.out.println(Matrix4x4.lookAt(randomVector3(), randomVector3(), Vector3.up()));
    }
    
    private static Vector3 randomVector3() {
        return new Vector3((float) Math.random(), (float) Math.random(), (float) Math.random());
    }
}
