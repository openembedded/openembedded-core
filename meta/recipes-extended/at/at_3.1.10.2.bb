require at.inc

LICENSE="GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4325afd396febcb659c36b49533135d4"

PR = "r0"

SRC_URI = "${DEBIAN_MIRROR}/main/a/at/at_${PV}.tar.gz \
    file://configure.patch \
    file://nonrootinstall.patch \
    file://use-ldflags.patch"

SRC_URI[md5sum] = "485688690a0aae53224c4150867da334"
SRC_URI[sha256sum] = "35c4ab4248ba5898ccaddc5efe8722a8f3639deeb07623fa2d41f740e337690f"
