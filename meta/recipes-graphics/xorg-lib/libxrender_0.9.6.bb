SUMMARY = "XRender: X Rendering Extension library"

DESCRIPTION = "The X Rendering Extension (Render) introduces digital \
image composition as the foundation of a new rendering model within the \
X Window System. Rendering geometric figures is accomplished by \
client-side tessellation into either triangles or trapezoids.  Text is \
drawn by loading glyphs into the server and rendering sets of them."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=0db75cc842842b36f097fdae571b4b70"

DEPENDS += "virtual/libx11 renderproto xproto xdmcp"

PR = "r0"
PE = "1"

XORG_PN = "libXrender"

BBCLASSEXTEND = "nativesdk"

SRC_URI[md5sum] = "3b3b7d076c2384b6c600c0b5f4ba971f"
SRC_URI[sha256sum] = "7f58b1e263109e0a873eef8423aa14733a5499befbe645053aa622ed1f3ea668"
