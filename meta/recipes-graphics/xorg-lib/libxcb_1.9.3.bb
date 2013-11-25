include libxcb.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d763b081cb10c223435b01e00dc0aba7"


DEPENDS += "libxdmcp"

PACKAGES =+ "libxcb-xinerama"

SRC_URI[md5sum] = "1ca999ca94f760d917ef2d8466a88742"
SRC_URI[sha256sum] = "aad09d223fcb5bd345ce4d1737f178a557b6f2e201128e1ee3c83af46028018b"
