include libxcb.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d763b081cb10c223435b01e00dc0aba7"

PR = "r0"

DEPENDS += "libxdmcp"

PACKAGES =+ "libxcb-xinerama"

SRC_URI[md5sum] = "2b05856e9d1cb37836aae7406f2f4ce2"
SRC_URI[sha256sum] = "8857e62b3aae2976c7e10043643e45a85964fd1dcb4469dfde0d04d3d1b12c96"
