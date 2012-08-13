require xorg-proto-common.inc

SUMMARY = "OpenGL: X OpenGL extension headers"

DESCRIPTION = "This package provides the wire protocol for the \
OpenGL-related extensions, used to enable the rendering of applications \
using OpenGL."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d44ed0146997856304dfbb512a59a8de \
                    file://glxproto.h;beginline=4;endline=32;md5=6b79c570f644363b356456e7d44471d9"

PR = "r0"
PE = "1"

BBCLASSEXTEND = "nativesdk"

SRC_URI[md5sum] = "3847963c1b88fd04a030b932b0aece07"
SRC_URI[sha256sum] = "54dd663a7f5ed702f2ff035b79edf770c2f850867ac0d55742f696bfc8b2598d"
