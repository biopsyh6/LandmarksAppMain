package com.pavlusha.landmarksapp.ui.screens.recognition

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader

@Composable
fun ARModelViewer(modelUrl: String) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val modelInstance = rememberModelInstance(modelLoader = modelLoader, fileLocation = modelUrl)

    var anchor by remember { mutableStateOf<Anchor?>(null) }

    ARSceneView(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        modelLoader = modelLoader,
        planeRenderer = true,
        onSessionUpdated = { _, frame ->
            if (anchor == null) {
                anchor = frame.getUpdatedPlanes()
                    .firstOrNull { plane ->
                        plane.type == Plane.Type.HORIZONTAL_UPWARD_FACING &&
                                plane.trackingState == TrackingState.TRACKING
                    }
                    ?.let { plane -> plane.createAnchor(plane.centerPose) }
            }
        }
    ) {
        anchor?.let { validAnchor ->
            AnchorNode(anchor = validAnchor) {

                modelInstance?.let { instance ->
                    ModelNode(
                        modelInstance = instance,
                        scaleToUnits = 0.5f,
                        autoAnimate = true
                    )
                }

            }
        }
    }
}