#pragma glslify: blend = require(../)

void main() {
  vec4 bgColor = texture2D(bg, vUv);
  vec4 fgColor = texture2D(foreground, vUv);

  vec3 color = blend(bgColor.rgb, fgColor.rgb);
  gl_FragColor = vec4(color, 1.0);
}
