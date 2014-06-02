SUMMARY = "Tune low-level block device parameters"
DESCRIPTION = "blktool is used for querying and/or changing settings \
of a block device. It is like hdparm but a more general tool, as it \
works on SCSI, IDE and SATA devices."
HOMEPAGE = "http://packages.debian.org/unstable/admin/blktool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://blktool.c;beginline=7;endline=8;md5=a5e798ea98fd50972088968a15e5f373"

DEPENDS = "glib-2.0"
PR = "r1"

SRC_URI = "${DEBIAN_MIRROR}/main/b/blktool/blktool_4.orig.tar.gz;name=tarball \
           ${DEBIAN_MIRROR}/main/b/blktool/blktool_4-6.1.diff.gz;apply=yes;name=patch"

SRC_URI[tarball.md5sum] = "62edc09c9908107e69391c87f4f3fd40"
SRC_URI[tarball.sha256sum] = "b1e6d5912546d2a4b704ec65c2b9664aa3b4663e7d800e06803330335a2cb764"

SRC_URI[patch.md5sum] = "cf605a683d54d1fbcb35b940076ddb0c"
SRC_URI[patch.sha256sum] = "d7923527e1a00984620ab307c9047a653d7ae2856cd20a82de8a33328f381ae3"

S = "${WORKDIR}/${BPN}-4.orig"

inherit autotools pkgconfig
