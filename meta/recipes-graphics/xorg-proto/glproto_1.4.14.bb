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

SRC_URI[md5sum] = "f48257daf0017f7a7667e5bf48ca3578"
SRC_URI[sha256sum] = "7aaf555a100a63c67dbffc63153dcaaa3de1a36e605a2e98fce63f64089ff999"
