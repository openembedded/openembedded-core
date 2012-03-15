SUMMARY = "XRender: X Rendering Extension library"

DESCRIPTION = "The X Rendering Extension (Render) introduces digital \
image composition as the foundation of a new rendering model within the \
X Window System. Rendering geometric figures is accomplished by \
client-side tessellation into either triangles or trapezoids.  Text is \
drawn by loading glyphs into the server and rendering sets of them."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=d8bc71986d3b9b3639f6dfd6fac8f196"

DEPENDS += "virtual/libx11 renderproto xproto xdmcp"

PR = "r0"
PE = "1"

XORG_PN = "libXrender"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "ee62f4c7f0f16ced4da63308963ccad2"
SRC_URI[sha256sum] = "f9b46b93c9bc15d5745d193835ac9ba2a2b411878fad60c504bbb8f98492bbe6"
