EGLINFO_PLATFORM ?= "fb"
EGLINFO_BINARY_NAME ?= "eglinfo-fb"

# .bbappend files in BSP meta layers can add EGLINFO_DEVICE
# values if necessary. eglinfo.inc contains default values for
# the currently supported devices.
# Consult the eglinfo README.md for details

include eglinfo.inc

SUMMARY += "(Framebuffer version)"
