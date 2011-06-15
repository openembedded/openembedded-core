require xorg-proto-common.inc
SRC_URI += "file://restore_the_old_xGLXBufferSwapComplete.patch"

SUMMARY = "OpenGL: X OpenGL extension headers"

DESCRIPTION = "This package provides the wire protocol for the \
OpenGL-related extensions, used to enable the rendering of applications \
using OpenGL."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d44ed0146997856304dfbb512a59a8de \
                    file://glxproto.h;beginline=4;endline=32;md5=6b79c570f644363b356456e7d44471d9"

PR = "r1"
PE = "1"

BBCLASSEXTEND = "nativesdk"

SRC_URI[md5sum] = "9542f2d36751a8ad7eae9d8e176f70d4"
SRC_URI[sha256sum] = "fc0a94d4df003cb6b6953173e6498d9c4c3268ee24bcc46a1172a1e1dbd3d742"
