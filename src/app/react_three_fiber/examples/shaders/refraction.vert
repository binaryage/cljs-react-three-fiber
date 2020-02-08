varying vec3 worldNormal;
varying vec3 viewDirection;

void main() {
    vec4 transformedNormal = vec4(normal, 0.);
    vec4 transformedPosition = vec4(position, 1.0);
    #ifdef USE_INSTANCING
    transformedNormal = instanceMatrix * transformedNormal;
    transformedPosition = instanceMatrix * transformedPosition;
    #endif

    vec4 worldPosition = modelMatrix * vec4(position, 1.0);
    worldNormal = normalize(modelViewMatrix * transformedNormal).xyz;
    viewDirection = normalize(worldPosition.xyz - cameraPosition);;
    gl_Position = projectionMatrix * modelViewMatrix * transformedPosition;
}
