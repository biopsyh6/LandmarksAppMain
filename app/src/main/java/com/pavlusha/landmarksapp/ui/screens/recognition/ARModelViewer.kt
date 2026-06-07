package com.pavlusha.landmarksapp.ui.screens.recognition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import com.pavlusha.domain.model.ARAnnotation
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.math.Position
import androidx.core.graphics.toColorInt
import com.google.ar.core.Config
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.math.Scale
import io.github.sceneview.rememberEnvironmentLoader

@Composable
fun ARModelViewer(
    modelUrl: String,
    annotations: List<ARAnnotation>,
    modelScale: Float = 0.5f,
    heightOffset: Float = 0.0f,
    rotationDegrees: Float = 0.0f
    ) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)

    val environmentLoader = rememberEnvironmentLoader(engine)

    val cameraNode = rememberARCameraNode(engine)

    val modelInstance = rememberModelInstance(modelLoader = modelLoader, fileLocation = modelUrl)

    val viewNodeManager = io.github.sceneview.rememberViewNodeManager()

    var anchor by remember { mutableStateOf<Anchor?>(null) }

    ARSceneView(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        modelLoader = modelLoader,
        environmentLoader = environmentLoader,
        cameraNode = cameraNode,
        planeRenderer = true,
        sessionConfiguration = { session, config ->
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
        },
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
                        scaleToUnits = modelScale,
                        position = Position(x = 0.0f, y = heightOffset, z = 0.0f),
                        rotation = io.github.sceneview.math.Rotation(
                            x = 0.0f,
                            y = rotationDegrees,
                            z = 0.0f
                        ),
                        autoAnimate = true
                    )
                }

                annotations.forEach { annotation ->
                    ViewNode(
                        windowManager = viewNodeManager,
                        position = Position(
                            x = annotation.positionX,
                            y = annotation.positionY,
                            z = annotation.positionZ
                        ),

                        scale = Scale(0.5f),
                        apply = {
                            onFrame = {
                                lookAt(cameraNode)

//                                rotation = io.github.sceneview.math.Rotation(
//                                    x = rotation.x,
//                                    y = rotation.y + 180f,
//                                    z = rotation.z
//                                )
                            }
                        },
                        viewContent = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xB3000000),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.3f), // Стеклянная полупрозрачная обводка
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = annotation.text,
                                        color = Color(annotation.colorHex.toColorInt()),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(24.dp)
                                        .background(Color.White.copy(alpha = 0.4f))
                                )

                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(color = Color.White, shape = CircleShape)
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}