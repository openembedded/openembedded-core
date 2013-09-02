EGLINFO_PLATFORM ?= "x11"
EGLINFO_BINARY_NAME ?= "eglinfo-x11"

include eglinfo.inc

DEPENDS += "virtual/libx11"

SUMMARY += "(X11 version)"
