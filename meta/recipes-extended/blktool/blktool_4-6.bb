DESCRIPTION = "blktool is used for querying and/or changing settings of a block device. It is like hdparm but a more general tool, as it works on SCSI, IDE and SATA devices"
HOMEPAGE = "http://packages.debian.org/unstable/admin/blktool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://blktool.c;beginline=7;endline=8;md5=a5e798ea98fd50972088968a15e5f373"
DEPENDS = "glib-2.0"

SRC_URI = "${DEBIAN_MIRROR}/main/b/blktool/blktool_4.orig.tar.gz \
           ${DEBIAN_MIRROR}/main/b/blktool/blktool_4-6.diff.gz;apply=yes"

PR = "r0"
S = "${WORKDIR}/${PN}-4.orig"

inherit autotools
