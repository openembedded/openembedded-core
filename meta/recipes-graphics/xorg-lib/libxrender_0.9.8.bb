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

PE = "1"

XORG_PN = "libXrender"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "2bd9a15fcf64d216e63b8d129e4f1f1c"
SRC_URI[sha256sum] = "1d14b02f0060aec5d90dfdcf16a996f17002e515292906ed26e3dcbba0f4fc62"
