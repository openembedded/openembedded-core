require mutter.inc

PR = "r0"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI += "file://nodocs.patch \
            file://nozenity.patch \
            file://fix_pkgconfig.patch \
           "
SRC_URI[archive.md5sum] = "ebe3c04751741fb2a028a3c6c4f1c2d7"
SRC_URI[archive.sha256sum] = "175f3adcc5ad5c6f23772ca15c862f275fc3d9a9c3104e9146cf265847a4a10a"

