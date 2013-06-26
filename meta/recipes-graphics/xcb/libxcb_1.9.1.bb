include libxcb.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d763b081cb10c223435b01e00dc0aba7"

PR = "r0"

DEPENDS += "libxdmcp"

PACKAGES =+ "libxcb-xinerama"

SRC_URI[md5sum] = "ed632cb0dc31b6fbd7ea5c0f931cf5a4"
SRC_URI[sha256sum] = "d44a5ff4eb0b9569e6f7183b51fdaf6f58da90e7d6bfc36b612d7263f83e362f"
