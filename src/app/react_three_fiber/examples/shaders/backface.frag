varying vec3 worldNormal;
void main() {
    gl_FragColor = vec4(worldNormal, 1.0);
}
